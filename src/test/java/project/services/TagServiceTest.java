package project.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import project.models.Tag;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("dev")
@TestPropertySource("/test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    @SneakyThrows
    public void getAllTagsTest(){
        List<Tag> tagList = tagService.getAllTags("tag", 0, 20);
        assertEquals(tagList.size(), 1);
    }

    @Test
    @SneakyThrows
    public void saveTagTest(){
        Tag tag = tagService.saveTag("tagForLife");
        assertEquals(tag.getTag(), "tagForLife");
    }

    @Test
    @SneakyThrows
    public void findByTagNameTest(){
        Tag tag = tagService.findByTagName("tag1");
        assertEquals(tag.getId(), 1);
    }

    @Test
    @SneakyThrows
    public void deleteTagTest(){
        tagService.deleteTag(2);
        Tag tag = tagService.findByTagName("gat2");
        assertNull(tag);
    }

}