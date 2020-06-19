package project.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.dto.responseDto.ListResponseDto;
import project.models.Friendship;
import project.models.Person;
import project.models.enums.FriendshipStatusCode;
import project.repositories.FriendshipRepository;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import java.util.List;

import static org.hamcrest.Matchers.hasValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
class FriendshipServiceTest {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private MockHttpServletRequest request;

    private MockHttpServletRequest request2;

    @Autowired
    public FriendshipServiceTest(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;

        request = new MockHttpServletRequest();
        request.addHeader("Authorization", tokenProvider.createToken("test1@mail.ru"));

        request2 = new MockHttpServletRequest();
        request2.addHeader("Authorization", tokenProvider.createToken("test2@mail.ru"));
    }

    @Test
    @Transactional
    void delete() {
        Friendship friendship = friendshipRepository.findById(34).get();
        friendshipService.delete(friendship);
        Friendship expected = friendshipRepository.findById(34).orElse(null);
        assertNull(expected);
        //Mockito.verify(friendshipRepository,Mockito.times(1)).findById(34);
    }

    @Test
    void save() {
        Friendship friendship = friendshipService.findById(34);
        friendship.getStatus().setCode(FriendshipStatusCode.FRIEND);
        friendship.setStatus(friendship.getStatus());

        friendshipService.save(friendship);

        Friendship ex = friendshipService.findById(34);
        assertEquals(ex.getStatus().getCode(), FriendshipStatusCode.FRIEND);

    }

    @Test
    void findByFriendsCouple() {

        Person firstFriend = personRepository.findById(10).get();
        Person secondFriend = personRepository.findById(2).get();
        Friendship friendship = friendshipService.findByFriendsCouple(firstFriend, secondFriend);
        assertEquals(friendship.getStatus().getCode(), FriendshipStatusCode.FRIEND);

    }

    @Test
    void findById() {
        Friendship friendship = friendshipService.findById(34);
        assertEquals(friendship.getStatus().getName(), "Запрос на добавление в друзья");
    }

    @Test
    void getFriendList() { // не судьба
        Person person = personRepository.findById(10).get();
        ListResponseDto<Person> dto = friendshipService.getFriendList("first2",0,20,person);
        assertEquals(dto.getTotal(), 1);

    }

    @Test
    void getFriendsList() {
        List<Person> personList = friendshipService.getFriendsList(personRepository.findById(10).get());
        assertEquals(personList.size(), 2);
    }


    @Test
    void getFriendRequests() {
        ListResponseDto dto = friendshipService.getFriendRequests("", 0,20,personRepository.findById(4).get());
        assertEquals(dto.getData().size(), 0);
    }

    @Test
    void sendFriendshipRequest() {
        friendshipService.sendFriendshipRequest(4, request2);
        Person person = personRepository.findById(4).orElse(null);
        assertEquals(person.getReceivedFriendshipRequests().size(),1);
    }

    @Test
    void deleteFriend() {
        friendshipService.deleteFriend(4, request);
        Person person = personRepository.findById(10).get();
        assertEquals(person.getReceivedFriendshipRequests().size(),2);
    }

}