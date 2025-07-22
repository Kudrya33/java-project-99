package hexlet.code.controller;

import hexlet.code.dto.userDto.UserCreateDto;
import hexlet.code.dto.userDto.UserDto;
import hexlet.code.dto.userDto.UserUpdateDto;
import hexlet.code.exeption.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8080"}, exposedHeaders = "X-Total-Count")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> index() {
        List<User> user = userRepository.findAll();
        return user.stream().map(userMapper::map).toList();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto show(@PathVariable long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("user with id " + id + " not found"));
        return userMapper.map(user);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserCreateDto dto) {
        User user = userMapper.map(dto);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.name")
    public UserDto update(@PathVariable long id, @Valid @RequestBody UserUpdateDto dto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("user with id " + id + " not found"));
        userMapper.update(dto, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.name")
    public void delete(@PathVariable long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("user with id " + id + " not found"));
        userRepository.delete(user);
    }
}
