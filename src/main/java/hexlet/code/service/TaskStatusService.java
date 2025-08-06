package hexlet.code.service;

import hexlet.code.dto.taskStatusDto.TaskStatusCreateDto;
import hexlet.code.dto.taskStatusDto.TaskStatusDto;
import hexlet.code.dto.taskStatusDto.TaskStatusUpdateDto;
import java.util.List;

public interface TaskStatusService {
    List<TaskStatusDto> getAll();
    TaskStatusDto getById(Long id);
    TaskStatusDto create(TaskStatusCreateDto dto);
    TaskStatusDto update(Long id, TaskStatusUpdateDto dto);
    void delete(Long id);
}
