package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.responseDto.ListResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.*;
import project.models.enums.FriendshipStatusCode;
import project.models.enums.NotificationTypeEnum;
import project.repositories.FriendshipRepository;
import project.repositories.NotificationRepository;
import project.repositories.NotificationTypeRepository;
import project.security.TokenProvider;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendshipService {

    private FriendshipRepository friendshipRepository;
    private PersonService personService;
    private TokenProvider tokenProvider;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationTypeRepository notificationTypeRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, PersonService personService,
                             TokenProvider tokenProvider) {
        this.friendshipRepository = friendshipRepository;
        this.personService = personService;
        this.tokenProvider = tokenProvider;
    }

    public void delete(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }

    public void save(Friendship friendship) {
        friendshipRepository.save(friendship);
    }

    public Friendship findByFriendsCouple(Person firstFriend, Person secondFriend){
        return friendshipRepository.findByFriendsCouple(firstFriend, secondFriend).orElse(null);
    }

    public Friendship findById(Integer id) {
        return friendshipRepository.findById(id).orElse(null);
    }

    // Метод Ильи для списка друзей
    public ListResponseDto<Person> getFriendList(String name, Integer offset, Integer itemPerPage, Person person) {

        List<Person> friendList = getFriendsList(person);

        if (name == null) {
            return new ListResponseDto<>((long) friendList.size(), offset, itemPerPage,
                    friendList.subList(offset, Math.min(friendList.size(), itemPerPage)));
        }

        List<Person> filterList = friendList.size() == 0 ? new ArrayList<>() : friendList.stream().filter(
                person1 -> person1.getFirstName().contains(name)
                        || person1.getLastName().contains(name))
                .collect(Collectors.toList());
        return new ListResponseDto<>((long) filterList.size(), offset, itemPerPage,
                filterList.subList(offset, Math.min(filterList.size(), itemPerPage)));
    }

    //====================================  FM  ===========================================================

    // Метод получения друзей для внутреннего пользования
    public List<Person> getFriendsList(Person person){
        List<Person> friends = person.getSentFriendshipRequests().stream().map(friendship -> {
            Person friend = null;
            if (friendship.getStatus().getCode().equals(FriendshipStatusCode.FRIEND)){
                friend = friendship.getDstPerson();
            }
            return friend;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        List<Person> friends1 = person.getReceivedFriendshipRequests().stream().map(friendship -> {
            Person friend = null;
            if (friendship.getStatus().getCode().equals(FriendshipStatusCode.FRIEND)){
                friend = friendship.getSrcPerson();
            }
            return friend;
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        friends.addAll(friends1);
        return friends.stream()
                .filter(friend -> friend.getBlockedBy() == null || !friend.getBlockedBy().equals(person.getId()))
                .collect(Collectors.toList());
    }

    public void getFriendRecursively(Person person, List<Person> resultList, int deep) {
        List<Person> friendList = getFriendsList(person);
        friendList.forEach(p -> {
            if (!resultList.contains(p))
                resultList.add(p);
        });
        if (deep > 0)
            friendList.forEach(f -> getFriendRecursively(f, resultList, deep - 1));
    }

    //=======================

    // Метод, возвращающий имеющиеся у пользователя заявки в друзья от других пользователей
    public ListResponseDto getFriendRequests(String name, Integer offset, Integer itemPerPage, Person person){

        List<Person> friendRequests = person.getReceivedFriendshipRequests().stream().map(friendship -> {
            Person friendRequester = null;
            if (friendship.getStatus().getCode().equals(FriendshipStatusCode.REQUEST))
                friendRequester = friendship.getSrcPerson();
            return friendRequester;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        //List<Person> friends = personRepository.findFriendRequests(person); // Старая версия
        return new ListResponseDto<>((long) friendRequests.size(), offset, itemPerPage, friendRequests);
    }

    //=========================

    @Transactional(rollbackFor = Exception.class)
    public void sendFriendshipRequest(Integer id, HttpServletRequest request) throws IllegalStateException {

        Person src = personService.getPersonByToken(request);
        Person dst = personService.findPersonById(id);

        if (src == dst) throw new BadRequestException400(); //шобы не отправить заявку самому себе, но нужно обработать нормально

        Friendship friendship = findByFriendsCouple(src, dst);
        if (friendship == null) {
            sendRequest(src, dst);
//            Notification notification = new Notification();
//            NotificationType notificationType = notificationTypeRepository.findByCode(NotificationTypeEnum.FRIEND_REQUEST);
//            notification.setMainEntity(src);
//            notification.setPerson(dst);
//            notification.setNotificationType(notificationType);
//            notification.setSentTime(new Date());
//            notificationRepository.save(notification);
        } else if (friendship.getStatus().getCode().equals(FriendshipStatusCode.REQUEST)){

            friendship.getStatus().setName(FriendshipStatusCode.FRIEND.getCode2Name());
            friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);

            List<Person> srcFriendList = getFriendsList(src);
            srcFriendList.remove(dst);
            List<Person> dstFriendList = getFriendsList(dst);
            dstFriendList.remove(src);

            srcFriendList.addAll(dstFriendList);

            List<Person> friendListBoth = srcFriendList.stream().distinct().collect(Collectors.toList());

            sendFriendshipAgreementToPersonFriends(friendListBoth, friendship);
        } else {
            throw new IllegalStateException("Fuck you!");
        }
    }

    private void sendFriendshipAgreementToPersonFriends(List<Person> friendList, Friendship friendship) {
        //List<Person> friendList = getFriendsList(person);
        for (Person friend : friendList) {
            if (!friend.equals(friendship.getSrcPerson()) && !friend.equals(friendship.getDstPerson())) {
                Notification notification = new Notification();
                NotificationType notificationType = notificationTypeRepository.findByCode(NotificationTypeEnum.FRIEND_REQUEST);
                notification.setMainEntity(friendship);
                notification.setPerson(friend);
                notification.setNotificationType(notificationType);
                notification.setSentTime(new Date());
                notificationRepository.save(notification);
            }
        }
    }

    private void sendRequest(Person src, Person dst){

        Friendship friendship = new Friendship();
        FriendshipStatus fs = new FriendshipStatus();
        fs.setName(FriendshipStatusCode.REQUEST.getCode2Name());
        fs.setCode(FriendshipStatusCode.REQUEST);
        fs.setTime(new Date());
        friendship.setSrcPerson(src);
        friendship.setDstPerson(dst);
        friendship.setStatus(fs);
        friendshipRepository.save(friendship);
    }

    public void deleteFriend(Integer id, HttpServletRequest request) {

        Person firstFriend = personService.getPersonByToken(request);
        Person secondFriend = personService.findPersonById(id);
        Friendship friendship = findByFriendsCouple(firstFriend, secondFriend);
        if (friendship != null){
            delete(friendship);
        }
    }

    // Метод одобрения заявки в друзья, пока не применить
    public void confirmFriendshipRequest(Person src, Person dst){

        Friendship friendship = findByFriendsCouple(src, dst);
        if (friendship != null) {
            friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);
        }
    }

    //============================================================================================================
}
