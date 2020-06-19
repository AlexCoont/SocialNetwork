package project.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.dto.dialog.request.CreateDialogDto;
import project.dto.dialog.request.MessageRequestDto;
import project.models.enums.ReadStatus;
import project.security.TokenProvider;
import project.services.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
class ApiDialogsControllerTest {

    @Autowired
    private MockMvc mvc;


    private static final ObjectMapper om = new ObjectMapper();
    private final String token;
    private final String token2;
    private final String BASE_PATH = "/api/v1/dialogs";

    @Autowired
    public ApiDialogsControllerTest(TokenProvider tokenProvider, PersonService personService) {
        token = tokenProvider.createToken("test1@mail.ru");
        token2 = tokenProvider.createToken("test2@mail.ru");
    }

    @Test
    @SneakyThrows
    @WithMockUser("ROLE_USER")
    void getAllDialogs() {
        System.out.println(token);

        mvc.perform(get(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(1)))
                .andExpect(jsonPath("$.offset",is(0)))
                .andExpect(jsonPath("$.perPage",is(20)))
                .andExpect(jsonPath("$.data[0].id", is(6)))
                .andExpect(jsonPath("$.data[0].unread_count", is(0)))
                .andExpect(jsonPath("$.data[0].last_message.author.email", is("test1@mail.ru")))
                .andExpect(jsonPath("$.data[0].last_message.recipient.email", is("test2@mail.ru")))
                .andExpect(jsonPath("$.data[0].last_message.message_text", is("hello world")));
    }

    @Test
    @SneakyThrows
    void createDialog() {

        List<Integer> list = new ArrayList<>(1);
        list.add(4);
        CreateDialogDto dto = new CreateDialogDto(list);
        System.out.println(om.writeValueAsString(dto));

        mvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))

                .header("Authorization",token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.id",is(7)));
    }


    @Test
    @SneakyThrows
    @WithMockUser("ROLE_USER")
    void countSentMessage() {

        mvc.perform(get(BASE_PATH + "/unreaded") .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count", is(0)));
    }

    @Test
    @SneakyThrows
    void getDialogMessages() {
        mvc.perform(get(BASE_PATH + "/6/messages").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(1)))
                .andExpect(jsonPath("$.offset",is(0)))
                .andExpect(jsonPath("$.perPage",is(20)))
                .andExpect(jsonPath("$.data.length()", is(1)))
                .andExpect(jsonPath("$.data[0].id", is(9)))
                .andExpect(jsonPath("$.data[0].time",is(1586153231000L)))
                .andExpect(jsonPath("$.data[0].author.email", is("test1@mail.ru")))
                .andExpect(jsonPath("$.data[0].recipient.email", is("test2@mail.ru")))
                .andExpect(jsonPath("$.data[0].isSentByMe", is(true)))
                .andExpect(jsonPath("$.data[0].read_status", is(ReadStatus.SENT.toString())));
    }

    @Test
    @SneakyThrows
    void sentMessage() {

        MessageRequestDto dto = new MessageRequestDto("Haba haba");

        mvc.perform(post(BASE_PATH + "/6/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recipientId", is(10)))
                .andExpect(jsonPath("$.data.authorId", is(2)))
                .andExpect(jsonPath("$.data.messageText", is("Haba haba")))
                .andExpect(jsonPath("$.data.readStatus", is("SENT")));

    }

    @Test
    @SneakyThrows
    void readMessage() {
        mvc.perform(put(BASE_PATH + "/6/messages/9/read")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",token2))
                .andDo(print())
                .andExpect(status().isOk());
    }
}