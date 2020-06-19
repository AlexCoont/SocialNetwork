package project.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.dto.dialog.request.MessageRequestDto;
import project.dto.dialog.response.DialogDto;
import project.dto.responseDto.ListResponseDto;
import project.models.Dialog;
import project.models.Message;
import project.models.Person;
import project.models.enums.ReadStatus;
import project.repositories.DialogRepository;
import project.repositories.MessageRepository;
import project.repositories.NotificationRepository;
import project.repositories.PersonRepository;
import project.security.TokenFilter;
import project.security.TokenProvider;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
class MessageServiceTest {


    @Autowired
    private MessageService messageService;


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private DialogRepository dialogRepository;

    @MockBean
    private NotificationRepository notificationRepository;

    @Autowired
    private PersonRepository personRepository;

    private MockHttpServletRequest request;

    private MockHttpServletRequest request3;

    @MockBean
    private TokenFilter tokenFilter;

    private TokenProvider tokenProvider;

    @Autowired
    public MessageServiceTest(TokenProvider tokenProvider) {
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", tokenProvider.createToken("test2@mail.ru"));

        request3 = new MockHttpServletRequest();
        request3.addHeader("Authorization", tokenProvider.createToken("test4@mail.ru"));
    }


//    @Before
//    public void setUp(){
//        request = new MockHttpServletRequest();
//        request.addHeader("Authorization", tokenProvider.createToken("test2@mail.ru"));
//    }


    @Test
    void findMessageById() {
        Message message = messageService.findMessageById(9);
        assertThat(message.getMessageText(), allOf(startsWith("hello"), endsWith("world")));

        //Mockito.verify(messageRepository,Mockito.times(1)).findById(90);
    }

    @Test
    @Transactional
    void getAllDialogs() {
         ListResponseDto<DialogDto> dto = messageService.getAllDialogs("???",0 , 10, request);
         assertEquals(dto.getData().size(), 1);
    }

    @Test
    void createDialog() {
        //messageService.sentMessage(60,new MessageRequestDto("test"), request);
//        messageService.createDialog(request3, new CreateDialogDto(asList(13,14)));
//
//        Person person = personRepository.findById(14).get();
//        assertEquals(person.getDialogs().size(), 1);

    }

    @Test
    void getCountSendMessage() {
        Integer count = messageService.getCountSendMessage(request);
        assertEquals(1, count);
    }

    @Test
    void getDialogMessages() {
        ListResponseDto dto = messageService.getDialogMessages(6,0,0, request);
        assertEquals(dto.getData().size(), 1);
    }

    @Test
    void sentMessage() {
        Person person = personRepository.findByEmail("test2@mail.ru").get();
        messageService.sentMessage(6,new MessageRequestDto("test"), person);
        Optional<Dialog> dialog = dialogRepository.findById(6);
        assertEquals(dialog.get().getListMessage().size(), 4);
    }

    @Test
    void readMessage() {
        messageService.readMessage(6, 9, request);
        Message message = messageService.findMessageById(9);
        assertEquals(message.getReadStatus(), ReadStatus.READ);
    }
}