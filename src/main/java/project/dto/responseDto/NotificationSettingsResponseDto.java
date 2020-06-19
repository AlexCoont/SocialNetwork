package project.dto.responseDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.enums.NotificationTypeEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
public class NotificationSettingsResponseDto {

    private String icon;

    private String name;

    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum type;

    private Boolean enable;

    public NotificationSettingsResponseDto(String icon, String name, NotificationTypeEnum type, Boolean enable) {
        this.icon = icon;
        this.name = name;
        this.type = type;
        this.enable = enable;
    }
}
