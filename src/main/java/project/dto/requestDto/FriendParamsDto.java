package project.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class FriendParamsDto {

    @Size(min = 2, max = 50)
    private String name;

    @PositiveOrZero()
    private Integer offset = 0;

    @Positive
    @Max(value = 50)
    private Integer limit = 20;
}
