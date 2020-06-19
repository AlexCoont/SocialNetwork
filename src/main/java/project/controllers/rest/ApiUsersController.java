package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.PostRequestBodyTagsDto;
import project.dto.requestDto.UpdatePersonDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.handlerExceptions.UnauthorizationException401;
import project.models.Person;
import project.security.TokenProvider;
import project.services.PersonService;
import project.services.PostService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users/")
@AllArgsConstructor
@Validated
public class ApiUsersController {

    private PersonService personService;
    private PostService postService;
    private TokenProvider tokenProvider;

    @GetMapping("me")
    public ResponseEntity<?> getAuthUser(HttpServletRequest servletRequest) throws UnauthorizationException401 {
        Person person = personService.getPersonByToken(servletRequest);
        person.setLastOnlineTime(new Date());
        personService.saveLastOnlineTime(person);
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @PutMapping("me")
    public ResponseEntity<?> personEditBody(@RequestBody UpdatePersonDto updatePersonDto,
                                                 HttpServletRequest request) throws UnauthorizationException401
    {
        Person person = personService.editBody(updatePersonDto, request);
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @DeleteMapping("me")
    public ResponseEntity<?> deleteUser(ServletRequest servletRequest) {
        Person person = personService.getPersonByToken(servletRequest);
        postService.deleteAllPostsByAuthorId(person.getId());
        personService.deletePersonByEmail(person.getEmail());
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Integer id, HttpServletRequest servletRequest) {
        Person personResponse = personService.findPersonById(id);
        Integer blockerId = personService.getPersonByToken(servletRequest).getId();
        Integer blockedBy = personResponse.getBlockedBy();
        if (blockedBy != null && blockedBy.equals(blockerId)) personResponse.setBlocked(true);
        return ResponseEntity.ok(new ResponseDto<>(personResponse));
    }

    @GetMapping("{id}/wall")
    public ResponseEntity<?> getWallPostsById(
            @PathVariable Integer id, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage, HttpServletRequest servletRequest)
            throws BadRequestException400 {
        int compareId = personService.getPersonByToken(servletRequest).getId();
        return ResponseEntity.ok(postService.findAllByAuthorId(id, offset, itemPerPage, compareId));
    }

    @PostMapping("{id}/wall")
    public ResponseEntity<?> addWallPostById(
            @PathVariable Integer id, @RequestParam(value = "publish_date", required = false) Long publishDate,
            @RequestBody PostRequestBodyTagsDto dto) throws BadRequestException400 {
        return ResponseEntity.ok(postService.addNewWallPostByAuthorId(id, publishDate, dto));
    }

    @PutMapping("block/{id}")
    public ResponseEntity<?> blockPersonById(@PathVariable Integer id, HttpServletRequest servletRequest) throws BadRequestException400 {    //обработать 400 и 401
        Person blocker = personService.getPersonByToken(servletRequest);
        personService.blockPersonById(id, true, blocker.getId());
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

    @DeleteMapping("block/{id}")
    public ResponseEntity<?> unblockPersonById(@PathVariable Integer id, HttpServletRequest servletRequest) throws BadRequestException400 { //обработать 400 и 401
        Person blocker = personService.getPersonByToken(servletRequest);
        personService.blockPersonById(id, false, blocker.getId());
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

    @GetMapping("search")
    ResponseEntity<?> search(@RequestParam(name = "first_name", required = false) @Size(max = 255) String firstName,
                             @RequestParam(name = "last_name", required = false) @Size(max = 255) String lastName,
                             @RequestParam(name = "age_from", required = false) Integer ageFrom,
                             @RequestParam(name = "age_to", required = false) Integer ageTo,
                             @RequestParam(required = false) @Size(max = 255) String country,
                             @RequestParam(required = false) @Size(max = 255) String city,
                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset,
                             @RequestParam(required = false, defaultValue = "20") @Positive @Max(20) Integer itemPerPage,
                             HttpServletRequest request) {
        Person person = personService.getPersonByToken(request);
        Page<Person> persons = personService.search(person, firstName, lastName, ageFrom, ageTo, country, city, offset, itemPerPage);
        return ResponseEntity.ok(new ListResponseDto<>(persons.getTotalElements(), offset, itemPerPage, persons.getContent()));
    }
}
