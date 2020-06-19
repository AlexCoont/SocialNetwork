package project.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "postList")
@Entity
@Table(name = "tag")
public class Tag
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String tag;

    @ManyToMany(mappedBy = "tagList", fetch = FetchType.LAZY)
    private Set<Post> postList = new HashSet<>();
}
