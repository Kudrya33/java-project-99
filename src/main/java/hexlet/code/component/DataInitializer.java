package hexlet.code.component;

import hexlet.code.dto.userDto.UserCreateDto;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
    }

    private void createAdmin() {
        String email = "hexlet@example.com";
        String password = "qwerty";
        String firstName = "admin";
        String lastName = "admin";
        UserCreateDto userData = new UserCreateDto();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        User user = userMapper.map(userData);
        userRepository.save(user);
    }
}
