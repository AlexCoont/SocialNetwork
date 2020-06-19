package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.AddTag;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Tag;
import project.services.TagService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tags/")
@AllArgsConstructor
public class ApiTagsController {

    private TagService tagService;

    @GetMapping
    public ResponseEntity<?> getTags(@RequestParam String query,
                                     @RequestParam(required = false) Integer offsetParam,
                                     @RequestParam(required = false) Integer limitParam)
    {
        List<Tag> tags = tagService.getAllTags(query, offsetParam, limitParam);

        return ResponseEntity.ok(new ListResponseDto<>((long) tags.size(), offsetParam, limitParam, tags));
    }

    @PostMapping
    public ResponseEntity<?> addTag(@RequestBody AddTag tagName){

        Tag tag = tagService.saveTag(tagName.getTag());

        return ResponseEntity.ok(new ResponseDto<>(tag));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTag(@RequestParam Integer tagId){
        tagService.deleteTag(tagId);
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }
}