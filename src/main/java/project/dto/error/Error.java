package project.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.dto.error.enums.ErrorEnum;

@Data
@AllArgsConstructor
public class Error {

    String error;

    @JsonProperty(value = "error_description")
    String errorDescription;



}
