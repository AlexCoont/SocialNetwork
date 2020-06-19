package project.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import project.repositories.PostLikeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Test
    void countLikesByPostId() {
        Integer count = postLikeService.countLikesByPostId(100);
        assertEquals(count, 2);
    }

    @Test
    void addLike() {
        postLikeService.addLike(4,100);
        Integer count = postLikeRepository.countAllByPostId(100);
        assertEquals(count, 3);
    }

    @Test
    void deleteLike() {
        postLikeService.deleteLike(100,2);
        Integer count = postLikeRepository.countAllByPostId(100);
        assertEquals(count, 1);
    }

    @Test
    void getAllPersonIdWhoLikedPost() {
        List<Integer> list = postLikeService.getAllPersonIdWhoLikedPost(100);
        assertEquals(list.get(0), 2);
        assertEquals(list.get(1),3);
    }
}