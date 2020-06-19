package project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.Person;
import project.models.PostComment;
import project.models.Tag;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Integer id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    private Person author;

    private String title;

    @JsonProperty("post_text")
    private String postText;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    private Integer likes;

    @JsonProperty("my_like")
    private Boolean myLike;

    private List<PostComment> comments;

    private List<String> tags;

    private String type;
}
