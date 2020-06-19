package project.models;

import lombok.Data;
import project.models.enums.NotificationTypeEnum;

import javax.persistence.*;

@Data
@Entity
@Table(name = "notification_type")
public class NotificationType
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private NotificationTypeEnum code;

    @Column(nullable = false)
    private String name;
}
