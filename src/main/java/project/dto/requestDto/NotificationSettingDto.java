package project.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.enums.NotificationTypeEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
public class NotificationSettingDto {

    @JsonProperty("notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum notificationType;

    private Boolean enable;
}
