package project.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordSetDto {
    String token;

    String password;
}
