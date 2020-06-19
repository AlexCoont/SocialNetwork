package project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentModelDto {

    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("comment_text")
    private String commentText;
}
