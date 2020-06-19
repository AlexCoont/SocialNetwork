package project.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import project.models.enums.MessagesPermission;

import javax.persistence.*;
import java.util.*;

@Data
@EqualsAndHashCode(
        callSuper = false)
@ToString(exclude = {"sentFriendshipRequests", "receivedFriendshipRequests",
        "notificationList", "notificationSettings", "postList", "dialogs", "commentList"})
@Entity
@Table(name = "person")
public class Person extends MainEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;

    @Column(name = "first_name", nullable = false)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @JsonProperty("last_name")
    private String lastName;

    @Column(updatable = false, name = "reg_date", nullable = false)
    @JsonProperty("reg_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date regDate;

    @Column(name = "birth_date")
    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date birthDate;

    @Column(name = "e_mail", unique = true, nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String photo;

    private String about;

    private String city;

    private String country;

    @JsonIgnore
    @Column(name = "confirmation_code")
    private String confirmationCode;

    @JsonIgnore
    @Column(name = "is_approved")
    @Type(type = "yes_no")
    private Boolean isApproved;

    @JsonProperty("messages_permission")
    @Column(name = "messages_permission")
    @Enumerated(EnumType.STRING)
    private MessagesPermission messagesPermission;

    @JsonProperty("last_online_time")
    @Column(name = "last_online_time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date lastOnlineTime;

    @JsonProperty("is_blocked")
    @Column(name = "is_blocked")
    @Type(type = "yes_no")
    private boolean isBlocked;

    @JsonIgnore
    @Column(name = "blocker_id")
    private Integer blockedBy;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "persons", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Dialog> dialogs;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<PostComment> commentList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "srcPerson", cascade = CascadeType.ALL)
    private List<Friendship> sentFriendshipRequests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dstPerson", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Friendship> receivedFriendshipRequests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Notification> notificationList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PersonNotificationSetting> notificationSettings = new ArrayList<>();

    @PreRemove
    public void removeUser() {
        roles.forEach(role -> role.getUsers().remove(this));
        dialogs.forEach(dialog -> dialog.getPersons().remove(this));
        notificationList.removeIf(notification -> notification.getMainEntity().getId() == this.getId());
    }
}
