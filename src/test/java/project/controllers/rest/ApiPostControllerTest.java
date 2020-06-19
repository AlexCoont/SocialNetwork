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
import project.dto.CommentModelDto;
import project.dto.requestDto.PostRequestBodyTagsDto;
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
class ApiPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApiPostController postController;

    private static final ObjectMapper om = new ObjectMapper();
    private final String token;
    private final String BASE_PATH = "/api/v1/post";

    @Autowired
    public ApiPostControllerTest(TokenProvider tokenProvider, PersonService personService) {
        token = tokenProvider.createToken("test2@mail.ru");
    }

    @Test
    @SneakyThrows
    void getPostById() {
        mockMvc.perform(get(BASE_PATH + "/100")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(100)));
    }

    @Test
    @SneakyThrows
    void editPostById() {
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        PostRequestBodyTagsDto dto = new PostRequestBodyTagsDto("Title", "edited",  tags);
        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(put(BASE_PATH + "/100")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(100)))
                .andExpect(jsonPath("$.data.title", is("Title")))
                .andExpect(jsonPath("$.data.post_text", is("edited")));
    }

    @Test
    @SneakyThrows
    void deletePostById() {
        mockMvc.perform(delete(BASE_PATH + "/100")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(100)));
    }

    @Test
    @SneakyThrows
    void findPostsByTitleAndDate() {
        mockMvc.perform(get(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .queryParam("text", "Title1")
                .queryParam("date_from", "1587036556085")
                .queryParam("date_to", "1587641334016"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(1)));
    }

    @Test
    @SneakyThrows
    void getAllComments() {
        mockMvc.perform(get(BASE_PATH + "/100/comments")
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(2)));
    }

    @Test
    @SneakyThrows
    void addNewComment() {
        CommentModelDto dto = new CommentModelDto(1, "test comment");

        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(post(BASE_PATH + "/100/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.post_id", is(100)))
                .andExpect(jsonPath("$.data.comment_text", is("test comment")));
    }
}