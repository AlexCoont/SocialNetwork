package project.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.dto.error.enums.ErrorDescriptionEnum;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDescription
{
    @JsonProperty(value = "error_description")
    ErrorDescriptionEnum errorDescription;
}
