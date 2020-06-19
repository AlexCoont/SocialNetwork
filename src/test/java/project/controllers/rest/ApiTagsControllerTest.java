package project.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.dto.requestDto.AddTag;
import project.repositories.TagRepository;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@WithMockUser("ROLE_USER")
public class ApiTagsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TagRepository repository;

    @Test
    @SneakyThrows
    public void getTagsTest(){
        mvc.perform(get("/api/v1/tags/")
                .param("offsetParam", "0")
                .param("limitParam", "10")
                .param("query", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total", is(0)));
    }

    @Test
    @SneakyThrows
    public void addTagTest(){
        AddTag addTag = new AddTag();
        addTag.setTag("taggggg");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(addTag);

        mvc.perform(post("/api/v1/tags/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.tag", is("taggggg")));
    }

    @Test
    @SneakyThrows
    public void deleteTagTest(){

        mvc.perform(delete("/api/v1/tags/")
                .param("tagId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
