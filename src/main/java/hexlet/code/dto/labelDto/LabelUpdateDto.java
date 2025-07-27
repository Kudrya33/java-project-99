package hexlet.code.dto.labelDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class LabelUpdateDto {
    @NotBlank
    private JsonNullable<String> name;
}
