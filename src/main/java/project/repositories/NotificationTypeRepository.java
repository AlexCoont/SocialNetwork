package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.NotificationType;
import project.models.enums.NotificationTypeEnum;

import java.util.Collection;
import java.util.List;

@Repository
public interface NotificationTypeRepository extends CrudRepository<NotificationType, Integer> {

    NotificationType findByCode(NotificationTypeEnum code);

    List<NotificationType> findByCodeIn(Collection<NotificationTypeEnum> codes);
}