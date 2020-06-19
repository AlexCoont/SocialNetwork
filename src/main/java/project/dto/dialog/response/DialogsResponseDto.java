package project.dto.dialog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.ResponseModel;

@Data
@AllArgsConstructor
public class DialogsResponseDto<T> extends ResponseModel
{
    private Integer offset = 0;

    private Integer total = 1;

    private Integer perPage = 10;

    private T data;

    public DialogsResponseDto(T data) {
        this.data = data;
    }

}
