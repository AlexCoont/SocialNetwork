package project.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.enums.MessagesPermission;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonDto {

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "birth_date")
    private Date birthDate;

    private String phone;

    @JsonProperty(value = "photo_id")
    private String photo;

    private String about;

    private String city;

    private String country;

    private MessagesPermission messagePermission;
}

