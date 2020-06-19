package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "tagList", callSuper = false)
@Entity
@ToString(exclude = "tagList")
@Table(name = "post")
public class Post extends MainEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "author_Id", nullable = false)
    private Person author;

    private String title;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private Boolean isBlocked;

    @JsonProperty("my_like")
    @Type(type = "yes_no")
    private Boolean myLike;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "post2tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tagList = new HashSet<>();

    @PreRemove
    public void removeTags() {
        tagList.forEach(tag -> tag.getPostList().remove(this));
    }
}
