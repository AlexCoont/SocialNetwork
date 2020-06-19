package project.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLikeDto {

    @JsonProperty("item_id")
    private Integer itemId;

    private String type;
}
