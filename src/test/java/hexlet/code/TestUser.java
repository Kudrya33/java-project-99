package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.userDto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUser {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MockMvc mockWvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    private User user;

    @BeforeEach
    public void repositoryPrepare() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
    }

    @Test
    public void testWelcome() throws Exception {
        MvcResult result = mockWvc.perform(get("/welcome"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring");
    }

    @Test
    public void testIndex() throws Exception {
        List<User> expectedUsers = userRepository.findAll();

        MvcResult result = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        List<UserDto> userDtos = om.readValue(body, new TypeReference<>() {});

        List<User> actualUsers = userDtos.stream()
                .map(dto -> {
                    User user = new User();
                    user.setId(dto.getId());
                    user.setEmail(dto.getEmail());
                    user.setFirstName(dto.getFirstName());
                    user.setLastName(dto.getLastName());
                    user.setCreatedAt(dto.getCreatedAt());
                    return user;
                })
                .toList();

        assertThat(actualUsers)
                .usingRecursiveComparison()
                .ignoringFields("password", "updatedAt")
                .isEqualTo(expectedUsers);
    }

    @Test
    public void testShow() throws Exception {

        MvcResult result = mockWvc.perform(get("/api/users/" + user.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(u -> u.node("email").isEqualTo(user.getEmail()),
                u -> u.node("firstName").isEqualTo(user.getFirstName()));
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("email", "email@list.com");
        data.put("password", "exe");
        data.put("firstName", "Pavel");
        data.put("lastName", "Kudrya");

        MvcResult result = mockWvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(data)).with(jwt()))
                .andExpect(status().isCreated())
                .andReturn();

        User user = userRepository.findByEmail("email@list.com").get();

        String body = result.getResponse().getContentAsString();

        assertThat(user.getLastName()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThatJson(body).and(u -> u.node("email").isEqualTo(user.getEmail()),
                u -> u.node("id").isEqualTo(user.getId()));
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>();
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token = jwt().
                jwt(builder -> builder.subject(user.getEmail()));
        data.put("email", "email@list.com");

        MvcResult result = mockWvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(data)).with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(u -> u.node("email").isEqualTo("email@list.com"));
    }

    @Test
    public void testDelete() throws Exception {
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token = jwt().
                jwt(builder -> builder.subject(user.getEmail()));
        ResultActions result = mockWvc.perform(delete("/api/users/" + user.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(user.getId())).isFalse();
    }
}
