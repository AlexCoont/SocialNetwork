package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class PostLike
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "post_id")
    private Integer postId;

}
