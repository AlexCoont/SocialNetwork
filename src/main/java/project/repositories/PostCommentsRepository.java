package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.PostComment;

import java.util.List;

@Repository
public interface PostCommentsRepository extends CrudRepository<PostComment, Integer>
{
    List<PostComment> findAllByPostIdAndIsBlocked(Integer postId, boolean isBlocked);


}
