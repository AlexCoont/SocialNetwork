package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.AddLikeDto;
import project.dto.responseDto.LikeUsersListDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.services.PersonService;
import project.services.PostLikeService;
import project.services.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/likes")
@AllArgsConstructor
public class ApiLikesController {

    private PostService postService;
    private PostLikeService postLikeService;
    private PersonService personService;

    @GetMapping
    public ResponseEntity<?> getAllLikesFromObject(@RequestParam(value = "item_id") Integer itemId,
                                                   @RequestParam(value = "type") String objectType){

        //Integer likeCount = postLikeService.countLikesByPostId(itemId);

        List<Integer> personsWhoLikedPost = postLikeService.getAllPersonIdWhoLikedPost(itemId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                new LikeUsersListDto(personsWhoLikedPost.size(), personsWhoLikedPost)));
    }

    @PutMapping
    public ResponseEntity<?> addLike(@RequestBody AddLikeDto addLikeDto, HttpServletRequest servletRequest){

        Person person = personService.getPersonByToken(servletRequest);

        List<Integer> personsWhoLikedItem = new ArrayList<>();

        if (addLikeDto.getType().equals("Post")) {
            postLikeService.addLike(person.getId(), addLikeDto.getItemId());
            postService.setMyLike(addLikeDto.getItemId(), true);

             personsWhoLikedItem.addAll(postLikeService.getAllPersonIdWhoLikedPost(addLikeDto.getItemId()));
        } else {
            personsWhoLikedItem.addAll(null);
        }

        return ResponseEntity.ok(
                new ResponseDto<>(
                new LikeUsersListDto(personsWhoLikedItem.size(), personsWhoLikedItem)));
    }

    @DeleteMapping
    public ResponseEntity<?> unLike(@RequestBody AddLikeDto addLikeDto, HttpServletRequest servletRequest){

        Person person = personService.getPersonByToken(servletRequest);

        Integer likeCount;

        if (addLikeDto.getType().equals("Post")) {
            postService.setMyLike(addLikeDto.getItemId(), false);
            likeCount = postLikeService.deleteLike(addLikeDto.getItemId(), person.getId());
        } else {
            likeCount = 0;
        }

        return ResponseEntity.ok(new ResponseDto<>(likeCount));
    }
}
