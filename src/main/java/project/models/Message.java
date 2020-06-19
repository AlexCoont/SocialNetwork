package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import project.models.enums.ReadStatus;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ToString(exclude = "dialog")
@Table(name = "message")
public class Message extends MainEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "recipient_id")
    private Integer recipientId;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "read_status")
    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;
}
