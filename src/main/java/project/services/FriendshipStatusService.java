package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.FriendshipStatusRepository;

@Service
@AllArgsConstructor
public class FriendshipStatusService {
    private FriendshipStatusRepository friendshipStatusRepository;

//    @PostConstruct
//    public void init() {
//        FriendshipStatus friendshipStatus = new FriendshipStatus();
//        friendshipStatus.setTime(new Date());
//        friendshipStatus.setName("Друзья");
//        friendshipStatus.setCode(FriendshipStatusCode.FRIEND);
//
//        friendshipStatusRepository.save(friendshipStatus);
//
//        FriendshipStatus friendshipStatus1 = new FriendshipStatus();
//        friendshipStatus1.setTime(new Date());
//        friendshipStatus1.setName("Заявка");
//        friendshipStatus1.setCode(FriendshipStatusCode.REQUEST);
//
//        friendshipStatusRepository.save(friendshipStatus1);
//    }
}
