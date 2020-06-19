package project.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponseDto {

    private String id;

    private Integer ownerId;

    private String fileName;

    private String relativeFilePath;

    private String rawFileURL;

    private String fileFormat;

    private Long bytes;

    private String fileType;

    private Long createdAt;

}

