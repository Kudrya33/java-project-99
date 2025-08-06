package hexlet.code.service;

import hexlet.code.dto.userDto.UserCreateDto;
import hexlet.code.dto.userDto.UserDto;
import hexlet.code.dto.userDto.UserUpdateDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    UserDto getById(Long id);
    UserDto create(UserCreateDto dto);
    UserDto update(Long id, UserUpdateDto dto);
    void delete(Long id);
}
