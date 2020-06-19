package project.dto.requestDto;

import java.util.List;

public class PostRequestBodyTagsDto extends PostRequestBodyDto {

    private List<String> tags;

    public PostRequestBodyTagsDto(String title, String postText, List<String> tags) {
        super(title, postText);
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
