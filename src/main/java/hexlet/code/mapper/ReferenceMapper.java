package hexlet.code.mapper;

import hexlet.code.exeption.ResourceNotFoundException;
import hexlet.code.model.BaseEntity;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    public TaskStatus toEntity(String slug) {
        return slug != null ? taskStatusRepository.findBySlug(slug).orElseThrow(() ->
                new ResourceNotFoundException("slug not found")) : null;
    }

    public List<Label> toEntity(List<Long> ids) {
        return ids != null ? ids.stream().map(id -> labelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("label with name " + id + " not found"))).toList() : null;
    }
}
