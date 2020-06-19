package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import project.models.enums.FriendshipStatusCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "friendship_status")
public class FriendshipStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    private String name;

    @Enumerated(EnumType.STRING)
    private FriendshipStatusCode code;
}
