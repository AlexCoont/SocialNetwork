package project.dto.dialog.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDialogDto {

    @JsonProperty("users_ids")
    private List<Integer> userIds;

}
