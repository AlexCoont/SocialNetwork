package project.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.enums.NotificationTypeEnum;

@Data
@AllArgsConstructor
public class NotificationRequestDto {

    private NotificationTypeEnum anEnum;
    private boolean enable;
}
