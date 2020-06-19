package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.NotificationType;
import project.models.Person;
import project.models.PersonNotificationSetting;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonNotificationSettingsRepository extends CrudRepository<PersonNotificationSetting, Integer> {

    List<PersonNotificationSetting> findAllByPerson(Person person);

    Optional<PersonNotificationSetting> findByNotificationTypeAndPerson(NotificationType notificationType, Person person);
}