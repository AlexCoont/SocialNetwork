package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.PostDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.NotificationDto;
import project.models.*;
import project.models.enums.NotificationTypeEnum;
import project.repositories.NotificationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@Slf4j
public class NotificationService {

    private final PersonService personService;
    private final MessageService messageService;
    private final PostService postService;
    private final FriendshipService friendshipService;
    private static final List<Integer> friendshipIds = new ArrayList<>();


    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               PersonService personService,
                               MessageService messageService,
                               PostService postService,
                               PostCommentsService postCommentsService, FriendshipService friendshipService) {
        this.personService = personService;
        this.messageService = messageService;
        this.postService = postService;
        this.friendshipService = friendshipService;
    }

    public ListResponseDto<NotificationDto> findAllNotificationsByPersonId(
            Person person, Integer offset, Integer itemsPerPage) {
        List<Notification> notificationList = person.getNotificationList().subList(
                offset, Math.min(itemsPerPage, person.getNotificationList().size()));
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        for (Notification notification : notificationList) {
                    NotificationTypeEnum notificationTypeCode = notification.getNotificationType().getCode();
                    Integer entityId = notification.getMainEntity().getId();

                    switch (notificationTypeCode) {
                        case POST:
                            PostDto postDto = postService.getPostDtoById(entityId, null);
                            notificationDtoList.add(new NotificationDto(
                                    notification.getId(),
                                    notificationTypeCode,
                                    notification.getSentTime(),
                                    postDto.getAuthor(),
                                    postDto.getTitle()
                            ));
                            break;
                        case POST_COMMENT:
                        case COMMENT_COMMENT:
                            break;
                        case FRIEND_BIRTHDAY:
                            break;
                        case FRIEND_REQUEST:
                            //if (friendshipIds.contains(entityId)) continue;

                            Friendship friendship = friendshipService.findById(entityId);
                            Person currentPerson = notification.getPerson();
                            Person friendshipSrcPerson = friendship.getSrcPerson();
                            Person friendshipDstPerson = friendship.getDstPerson();
                            List<Person> friendList = friendshipService.getFriendsList(currentPerson);

                            if (friendList.contains(friendshipSrcPerson)) {
                                notificationDtoList.add(new NotificationDto(
                                        notification.getId(),
                                        notificationTypeCode,
                                        notification.getSentTime(),
                                        friendshipSrcPerson,
                                        friendshipDstPerson.getFirstName() + " " +
                                                friendshipDstPerson.getLastName()
                                ));
                            }

                            if (friendList.contains(friendshipDstPerson)) {
                                notificationDtoList.add(new NotificationDto(
                                        notification.getId(),
                                        notificationTypeCode,
                                        notification.getSentTime(),
                                        friendshipDstPerson,
                                        friendshipSrcPerson.getFirstName() + " " +
                                                friendshipSrcPerson.getLastName()
                                ));
                            }
                            break;
                        case MESSAGE:
                            Message message = messageService.findMessageById(entityId);
                            if (message != null) {
                                Person author = personService.findPersonById(message.getAuthorId());
                                if (author == null || author.getId() == person.getId()) continue;
                                notificationDtoList.add(new NotificationDto(
                                        notification.getId(),
                                        notificationTypeCode,
                                        notification.getSentTime(),
                                        author,
                                        message.getMessageText()
                                ));
                            }
                            break;
                    }
        }
        notificationDtoList.sort(Comparator.comparing(NotificationDto::getSentTime).reversed());
        return new ListResponseDto<>((long) notificationDtoList.size(), offset, itemsPerPage, notificationDtoList);
    }

//    private MainEntity getEntityById(Integer entityId, NotificationTypeEnum notificationTypeCode) {
//        MainEntity entity = null;
//        switch (notificationTypeCode) {
//            case POST:
//                PostDto postDto = postService.getPostDtoById(entityId, null);
//
//                break;
//            case POST_COMMENTS:
//            case COMMENT_COMMENT:
//                break;
//            case FRIEND_BIRTHDAY:
//                break;
//            case FRIEND_REQUEST:
//                entity = personService.findPersonById(entityId);
//                break;
//            case MESSAGE:
//                break;
//        }
//        return entity;
//    }
}
