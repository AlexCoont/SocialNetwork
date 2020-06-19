package project.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import project.dto.CommentModelDto;
import project.models.PostComment;
import project.repositories.PersonRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;


@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
class PostCommentsServiceTest {

    @Autowired
    private PostCommentsService postCommentsService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void getListComments() {
        List<PostComment> list = postCommentsService.getListComments(100);
        assertEquals(list.size(),2);
        assertEquals(list.get(0).getComment(),"comment1");
    }

    @Test
    void addNewCommentToPost() {
        postCommentsService.addNewCommentToPost(100,new CommentModelDto(400, "test"), personRepository.findById(2).get());
        List<PostComment> list = postCommentsService.getListComments(100);
        assertEquals(list.size(),3);
        assertEquals(list.get(2).getComment(), "test");
    }
}