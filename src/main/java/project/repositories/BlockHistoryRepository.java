package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.BlockHistory;

@Repository
public interface BlockHistoryRepository extends CrudRepository<BlockHistory, Integer> {
}
