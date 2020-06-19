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
import project.dto.requestDto.LoginRequestDto;
import project.security.TokenProvider;
import project.services.PersonService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class ApiAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper om = new ObjectMapper();
    private final String token2;

    @Autowired
    public ApiAuthControllerTest(TokenProvider tokenProvider, PersonService personService) {
        token2 = tokenProvider.createToken("test2@mail.ru");
    }

    @Test
    @SneakyThrows
    void login() {
        LoginRequestDto dto = new LoginRequestDto("test2@mail.ru", "123123123");
        String json = om.writeValueAsString(dto);
        System.out.println(json);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(2)));
    }

    @Test
    @SneakyThrows
    @WithMockUser("ROLE_USER")
    void logout() {
        mockMvc.perform(post("/api/v1/auth/logout")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message", is("ok")));
    }
}