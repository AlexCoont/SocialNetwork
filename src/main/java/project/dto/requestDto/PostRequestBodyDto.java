package project.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequestBodyDto {

    private String title;

    @JsonProperty("post_text")
    private String postText;
}
