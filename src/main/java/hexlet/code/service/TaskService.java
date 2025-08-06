package hexlet.code.service;

import hexlet.code.dto.taskDto.TaskCreateDto;
import hexlet.code.dto.taskDto.TaskDto;
import hexlet.code.dto.taskDto.TaskParamsDto;
import hexlet.code.dto.taskDto.TaskUpdateDto;
import java.util.List;

public interface TaskService {
    List<TaskDto> getAll(TaskParamsDto params);
    TaskDto getById(Long id);
    TaskDto create(TaskCreateDto dto);
    TaskDto update(Long id, TaskUpdateDto dto);
    void delete(Long id);
}
