package project.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import project.models.ResponseModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseDto<T> extends ResponseModel {

    private T data;

    public ResponseDto(T data) {
        this.data = data;
    }
}
