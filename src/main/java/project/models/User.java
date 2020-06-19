package project.models;

import lombok.Data;
import project.models.enums.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @Column(name = "e_mail")
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Type type;
}
