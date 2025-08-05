package hexlet.code.service;

import hexlet.code.dto.labelDto.LabelCreateDto;
import hexlet.code.dto.labelDto.LabelDto;
import hexlet.code.dto.labelDto.LabelUpdateDto;
import java.util.List;

public interface LabelService {
    List<LabelDto> getAll();
    LabelDto getById(Long id);
    LabelDto create(LabelCreateDto dto);
    LabelDto update(Long id, LabelUpdateDto dto);
    void delete(Long id);
}
