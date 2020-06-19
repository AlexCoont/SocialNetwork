package project.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import project.models.enums.RoleEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleEnum name;

    @Setter
    @Getter
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Person> users;

    @Override
    public String getAuthority() {
        return getName().name();
    }
}