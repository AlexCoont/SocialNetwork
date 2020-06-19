package project.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import project.dto.requestDto.AddLikeDto;
import project.security.TokenProvider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@WithMockUser("ROLE_USER")
@TestPropertySource("/test.properties")
class ApiLikesControllerTest {

    @Autowired
    private MockMvc mvc;

    private final String token2;

    @Autowired
    public ApiLikesControllerTest(TokenProvider tokenProvider) {
        token2 = tokenProvider.createToken("test2@mail.ru");
    }

    @SneakyThrows
    @Test
    public void getAllLikesFromObject() {
        mvc.perform(get("/api/v1/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .param("item_id", "100")
                .param("type", "Post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    public void addLike() {
        AddLikeDto addLikeDto = new AddLikeDto();
        addLikeDto.setItemId(100);
        addLikeDto.setType("Post");

        String requestJson = getString(addLikeDto);

        mvc.perform(put("/api/v1/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token2)
                .content(requestJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SneakyThrows
    @Test
    public void unLike() {
        AddLikeDto addLikeDto = new AddLikeDto();
        addLikeDto.setItemId(100);
        addLikeDto.setType("Post");

        String requestJson = getString(addLikeDto);

        mvc.perform(delete("/api/v1/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token2)
                .content(requestJson))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private String getString(AddLikeDto addLikeDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(addLikeDto);
    }
}