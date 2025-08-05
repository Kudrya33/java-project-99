package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import net.datafaker.Faker;
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

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTask {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    private Task task;
    private Label label;
    private User user;
    private TaskStatus taskStatus;
    private TaskStatus taskStatusForFilter;
    private Task taskFilter;

    @BeforeEach
    public void repositoryPrepare() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();

        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        taskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .create();

        taskStatusForFilter = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .create();

        label = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getCreatedAt))
                .create();

        labelRepository.save(label);
        userRepository.save(user);
        taskStatusRepository.save(taskStatus);
        taskStatusRepository.save(taskStatusForFilter);

        Set<Label> labels = new HashSet<>();
        labels.add(label);

        task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getCreatedAt))
                .supply(Select.field(Task::getTaskLabel), () -> labels)
                .supply(Select.field(Task::getAssignee), () -> user)
                .supply(Select.field(Task::getTaskStatus), () -> taskStatus)
                .create();

        taskFilter = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getCreatedAt))
                .supply(Select.field(Task::getTaskLabel), () -> labels)
                .supply(Select.field(Task::getAssignee), () -> user)
                .supply(Select.field(Task::getTaskStatus), () -> taskStatusForFilter)
                .create();

        taskRepository.save(task);
        taskRepository.save(taskFilter);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks/" + task.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                t -> t.node("index").isEqualTo(task.getIndex()),
                t -> t.node("status").isEqualTo(taskStatus.getSlug()),
                t -> t.node("assignee_id").isEqualTo(user.getId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("index", "12345");
        data.put("assignee_id", String.valueOf(user.getId()));
        data.put("title", "Some Title");
        data.put("status", taskStatus.getSlug());

        MvcResult result = mockMvc.perform(post("/api/tasks").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();

        Task resultTask = taskRepository.findByName("Some Title").get();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("index").isEqualTo("12345"));
        assertThat(resultTask.getCreatedAt()).isNotNull();
        assertThat(resultTask.getDescription()).isNull();
        assertThat(resultTask.getName()).isEqualTo("Some Title");
    }

    @Test
    public void updateTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("title", "New title");
        data.put("content", "New content");

        MvcResult result = mockMvc.perform(put("/api/tasks/" + task.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                t -> t.node("title").isEqualTo("New title"),
                t -> t.node("content").isEqualTo("New content")
        );
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + task.getId()).with(jwt()))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(task.getId())).isFalse();
    }

    @Test
    public void testFilter() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("assigneeId", String.valueOf(user.getId()));
        data.put("status", taskStatus.getSlug());

        MvcResult result = mockMvc.perform(get("/api/tasks").with(jwt())
                        .param("assigneeId", String.valueOf(user.getId()))
                        .param("status", taskStatus.getSlug()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains(task.getName());
        assertThat(body).doesNotContain(taskFilter.getName());
    }
}
