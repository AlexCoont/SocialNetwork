package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
}
