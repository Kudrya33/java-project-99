package hexlet.code.dto.labelDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabelCreateDto {
    @NotBlank
    private String name;
}
