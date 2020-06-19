package project.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.dto.requestDto.PostRequestBodyTagsDto;
import project.dto.requestDto.UpdatePersonDto;
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
class ApiUsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();
    private final String token;
    private final String token2;
    private final String BASE_PATH = "/api/v1/users";

    @Autowired
    public ApiUsersControllerTest(TokenProvider tokenProvider, PersonService personService) {
        token2 = tokenProvider.createToken("test4@mail.ru");
        token = tokenProvider.createToken("test2@mail.ru");
    }

    @Test
    @SneakyThrows
    void getAuthUser() {
        mockMvc.perform(get(BASE_PATH + "/me")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(2)))
                .andExpect(jsonPath("$.data.email", is("test2@mail.ru")));
    }

    @Test
    @SneakyThrows
    void personEditBody() {
        UpdatePersonDto dto = new UpdatePersonDto();
        dto.setFirstName("firstOne");
        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(put(BASE_PATH + "/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(2)))
                .andExpect(jsonPath("$.data.first_name", is("firstOne")));
        //.andExpect(jsonPath("$.data.last_name", is()));
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        mockMvc.perform(delete(BASE_PATH + "/me")
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("ok")));
    }

    @Test
    @SneakyThrows
    void getPersonById() {
        mockMvc.perform(get(BASE_PATH + "/2")
                //.param("id", "2")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(2)));
    }

    @Test
    @SneakyThrows
    void getWallPostsById() {
        mockMvc.perform(get(BASE_PATH + "/2/wall")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title", is("Title2")))
                .andExpect(jsonPath("$.data[1].title", is("Title1")));
    }

    @Test
    @SneakyThrows
    void addWallPostById() {
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        PostRequestBodyTagsDto dto = new PostRequestBodyTagsDto("test post", "it's a test!",  tags);
        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(post(BASE_PATH + "/2/wall")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("test post")));
    }

    @Test
    @SneakyThrows
    void blockPersonById() {   //не нужно ли никак проверять поле blocker у юзера?
        mockMvc.perform(put(BASE_PATH + "/block/2")
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("ok")));
    }

    @Test
    @SneakyThrows
    void unblockPersonById() {
        mockMvc.perform(delete(BASE_PATH + "/block/2")
                .header("Authorization", token2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("ok")));
    }

    @Test
    @SneakyThrows
    void search() {
        mockMvc.perform(get(BASE_PATH + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .param("first_name", "first4")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(1)))
                .andExpect(jsonPath("$.data[0].id",is(4)))
                .andExpect(jsonPath("$.data[0].email",is("test4@mail.ru")))
                .andExpect(jsonPath("$.data[0].first_name",is("first4")));
    }
}