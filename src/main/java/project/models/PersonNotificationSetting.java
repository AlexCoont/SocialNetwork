package project.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name = "person_notification_settings")
@NoArgsConstructor
public class PersonNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;


    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;

    @Column(name = "enable")
    @Type(type = "yes_no")
    private Boolean enable;

    public PersonNotificationSetting(Person person, NotificationType notificationType, Boolean enable) {
        this.person = person;
        this.notificationType = notificationType;
        this.enable = enable;
    }
}
