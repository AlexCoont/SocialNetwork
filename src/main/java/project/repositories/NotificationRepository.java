package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Notification;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {

}
