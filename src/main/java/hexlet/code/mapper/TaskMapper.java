package hexlet.code.mapper;

import hexlet.code.dto.taskDto.TaskCreateDto;
import hexlet.code.dto.taskDto.TaskDto;
import hexlet.code.dto.taskDto.TaskUpdateDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "taskLabelIds", target = "taskLabel", qualifiedByName = "mapLabels")
    public abstract Task map(TaskCreateDto data);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "taskLabel", target = "taskLabelIds", qualifiedByName = "mapTaskLabel")
    public abstract TaskDto map(Task task);

    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "taskLabelIds", target = "taskLabel", qualifiedByName = "mapLabels")
    public abstract void update(TaskUpdateDto data, @MappingTarget Task task);

    @Named("mapTaskLabel")
    List<Long> mapTaskLabel(Set<Label> labels) {
        return labels == null ? null : labels.stream().map(Label::getId).collect(Collectors.toList());
    }

    @Named("mapLabels")
    Set<Label> mapLabels(List<Long> ids) {
        return ids == null ? null : ids.stream()
                .map(id -> {
                    Label label = new Label();
                    label.setId(id);
                    return label;
                })
                .collect(Collectors.toSet());
    }
}
