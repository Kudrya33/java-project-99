package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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
public class TestLabel {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    private Label label;

    @BeforeEach
    public void repositoryPrepare() {
        labelRepository.deleteAll();
        label = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getCreatedAt))
                .create();

        labelRepository.save(label);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/labels").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/labels/" + label.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(l -> l.node("name").isEqualTo(label.getName()),
                l -> l.node("id").isEqualTo(label.getId()));
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Test name");

        MvcResult result = mockMvc.perform(post("/api/labels").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();

        Label resultLabel = labelRepository.findByName("Test name").get();

        String body = result.getResponse().getContentAsString();
        assertThat(resultLabel.getId()).isNotNull();
        assertThat(resultLabel.getCreatedAt()).isNotNull();
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Update name");

        MvcResult result = mockMvc.perform(put("/api/labels/" + label.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        Label resultLabel = labelRepository.findByName("Update name").get();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(l -> l.node("name").isEqualTo("Update name"));
        assertThat(resultLabel.getName()).isEqualTo("Update name");
    }

    @Test
    public void testDelete() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/labels/" + label.getId()).with(jwt()))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(labelRepository.existsById(label.getId())).isFalse();
    }
}
