package project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.dto.responseDto.NotificationSettingsResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.NotificationType;
import project.models.Person;
import project.models.PersonNotificationSetting;
import project.repositories.PersonNotificationSettingsRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class PersonNotificationSettingsService {

    private PersonNotificationSettingsRepository personNotificationSettingsRepository;

    public void add(Person person, boolean enable, List<NotificationType> types) {
        List<PersonNotificationSetting> settings = new ArrayList<>();
        for(NotificationType type : types)
            settings.add(new PersonNotificationSetting(person, type, enable));
        personNotificationSettingsRepository.saveAll(settings);
    }

    public void save(PersonNotificationSetting notificationSetting){
        personNotificationSettingsRepository.save(notificationSetting);
    }

    public ResponseDto<List<NotificationSettingsResponseDto>> findAllByPerson(Person person) {
        List<PersonNotificationSetting> settingList = person.getNotificationSettings();
        List<NotificationSettingsResponseDto> dtoSettingList = settingList.stream().map(
                    setting -> new NotificationSettingsResponseDto(
                            "",
                            setting.getNotificationType().getName(),
                            setting.getNotificationType().getCode(),
                            setting.getEnable()))
                    .collect(toList());
        return new ResponseDto<>(dtoSettingList);
    }

    public ResponseDto<PersonNotificationSetting> updateNotificationSetting(
            Person person, NotificationType notificationType) {
        PersonNotificationSetting setting = personNotificationSettingsRepository
                .findByNotificationTypeAndPerson(notificationType, person).orElse(null);
        if (setting != null){
            if (setting.getEnable()) {
                setting.setEnable(false);
            } else {
                setting.setEnable(true);
            }
        }
        return new ResponseDto<>(personNotificationSettingsRepository.save(setting));
    }
}
