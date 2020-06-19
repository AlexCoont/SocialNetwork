package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.PostFiles;

@Repository
public interface PostFileRepository extends CrudRepository<PostFiles, Integer> {
}
