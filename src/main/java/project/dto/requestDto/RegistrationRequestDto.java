package project.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegistrationRequestDto {

    @Email
    private String email;

    @NotBlank
    @Size(max = 72)
    private String passwd1;

    @NotBlank
    @Size(max = 72)
    private String passwd2;

    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotBlank
    @Size(max = 255)
    private String lastName;

    private String code;
}
