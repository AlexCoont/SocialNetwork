package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "post_comments")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    @JsonProperty("post_id")
    @Column(name = "post_id")
    private Integer postId;

    @JsonProperty("parent_id")
    @Column(name = "parent_id")
    private Integer parentId;

    @JsonProperty("author")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @JsonProperty("comment_text")
    @Column(name = "comment", nullable = false, columnDefinition = "text")
    private String comment;

    @JsonProperty("is_blocked")
    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private Boolean isBlocked;

    @JsonProperty("my_like")
    @Type(type = "yes_no")
    private Boolean myLike;
}

