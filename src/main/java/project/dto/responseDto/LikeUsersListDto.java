package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.Person;

import java.util.List;

@Data
@AllArgsConstructor
public class LikeUsersListDto {

    private Integer likes;

    private List<Integer> users;

}
