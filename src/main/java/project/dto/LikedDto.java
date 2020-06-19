package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import project.models.ResponseModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class LikedDto extends ResponseModel {
    private Boolean likes;
}
