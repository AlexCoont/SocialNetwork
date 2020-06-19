package project.dto.dialog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import project.models.ResponseModel;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageSetResponseDto<T> extends ResponseModel {

    private Long total;

    private Integer offset;

    private Integer perPage;

    private Set<T> data;
}