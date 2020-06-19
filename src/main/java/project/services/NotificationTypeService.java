package project.services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import project.handlerExceptions.EntityNotFoundException;
import project.models.NotificationType;
import project.models.enums.NotificationTypeEnum;
import project.repositories.NotificationTypeRepository;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationTypeService {

    private NotificationTypeRepository notificationTypeRepository;

    public NotificationType findByCode(NotificationTypeEnum code) {
        return notificationTypeRepository.findByCode(code);
    }

    @SneakyThrows(EntityNotFoundException.class)
    public List<NotificationType> findByCode(NotificationTypeEnum... codes) {
        List<NotificationType> types = notificationTypeRepository.findByCodeIn(Arrays.asList(codes));

        if (types.size() != codes.length)
            throw new EntityNotFoundException("Can't find all notification types");

        return types;
    }

    public NotificationType findById(Integer id){
        return notificationTypeRepository.findById(id).orElse(null);
    }
}