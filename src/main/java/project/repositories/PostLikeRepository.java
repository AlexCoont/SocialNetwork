package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.models.PostLike;

import java.util.List;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Integer> {
    Integer countAllByPostId(Integer postId);

    @Transactional
    Integer deleteByPostIdAndPersonId(Integer postId, Integer personId);

    List<PostLike> findAllByPostId(Integer postId);
}
