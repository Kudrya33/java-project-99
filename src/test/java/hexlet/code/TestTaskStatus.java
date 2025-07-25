package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTaskStatus {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper om;

    private TaskStatus taskStatus;

    @BeforeEach
    public void repositoryPrepare() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        taskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .ignore(Select.field((TaskStatus::getCreatedAt)))
                .ignore(Select.field(TaskStatus::getTasks))
                .create();
        taskStatusRepository.save(taskStatus);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/task_statuses/" + taskStatus.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo(taskStatus.getName()),
                t -> t.node("createdAt").isNotNull());
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Test");
        data.put("slug", "test");

        MvcResult mvcResult = mockMvc.perform(post("/api/task_statuses").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();

        TaskStatus task = taskStatusRepository.findBySlug("test").get();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo("Test"));
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getId()).isNotNull();
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Updated");

        MvcResult mvcResult = mockMvc.perform(put("/api/task_statuses/" + taskStatus.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo("Updated"));
    }

    @Test
    public void testDelete() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(delete("/api/task_statuses/" + taskStatus.getId()).with(jwt()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(taskStatusRepository.existsById(taskStatus.getId())).isFalse();
    }
}
