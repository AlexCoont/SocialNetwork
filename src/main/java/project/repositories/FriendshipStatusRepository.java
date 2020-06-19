package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.FriendshipStatus;

@Repository
public interface FriendshipStatusRepository extends CrudRepository<FriendshipStatus, Integer> {
}
