package hexlet.code.controller;

import hexlet.code.dto.labelDto.LabelCreateDto;
import hexlet.code.dto.labelDto.LabelDto;
import hexlet.code.dto.labelDto.LabelUpdateDto;
import hexlet.code.exeption.ResourceNotFoundException;
import hexlet.code.exeption.UnprocessableEntity;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<LabelDto> index() {
        List<LabelDto> labels = labelRepository.findAll().stream().map(labelMapper::map).toList();
        return labels;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDto show(@PathVariable long id) {
        Label label = labelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("label with id " + id + " not found"));
        LabelDto labelDto = labelMapper.map(label);
        return labelDto;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDto create(@Valid @RequestBody LabelCreateDto data) {
        Label label = labelMapper.map(data);
        labelRepository.save(label);
        LabelDto labelDto = labelMapper.map(label);
        return labelDto;
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDto update(@PathVariable long id, @Valid @RequestBody LabelUpdateDto data) {
        Label label = labelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("label with id " + id + " not found"));
        labelMapper.update(data, label);
        labelRepository.save(label);
        LabelDto labelDto = labelMapper.map(label);
        return labelDto;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        Label label = labelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("label with id " + id + " not found"));
        try {
            labelRepository.delete(label);
        } catch (Exception e) {
            throw new UnprocessableEntity("Cannot be completed, label connected with task");
        }
    }
}
