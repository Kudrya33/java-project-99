package hexlet.code.mapper;

import hexlet.code.dto.taskStatusDto.TaskStatusCreateDto;
import hexlet.code.dto.taskStatusDto.TaskStatusDto;
import hexlet.code.dto.taskStatusDto.TaskStatusUpdateDto;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatus map(TaskStatusCreateDto dto);
    public abstract TaskStatusDto map(TaskStatus status);
    public abstract void update(TaskStatusUpdateDto dto, @MappingTarget TaskStatus status);
}
