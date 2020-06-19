package project.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OffsetLimitDto {
    private Integer offset;
    private Integer limit;
}
