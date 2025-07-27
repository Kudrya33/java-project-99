package hexlet.code.dto.labelDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class LabelDto {
    private long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
