package hexlet.code.mapper;

import hexlet.code.dto.labelDto.LabelCreateDto;
import hexlet.code.dto.labelDto.LabelDto;
import hexlet.code.dto.labelDto.LabelUpdateDto;
import hexlet.code.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {
    public abstract Label map(LabelCreateDto data);
    public abstract LabelDto map(Label label);
    public abstract void update(LabelUpdateDto data, @MappingTarget Label label);
}
