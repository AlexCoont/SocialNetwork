package project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Getter
    @Setter
    private Integer id;

    @JsonIgnore
    @Getter
    @OneToMany(mappedBy = "mainEntity", cascade = CascadeType.ALL)
    protected List<Notification> sentNotifications = new ArrayList<>();
}
