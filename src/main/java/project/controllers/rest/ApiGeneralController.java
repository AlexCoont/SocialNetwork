package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.LikedDto;
import project.dto.PostDto;
import project.dto.responseDto.FileUploadResponseDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.handlerExceptions.EntityNotFoundException;
import project.models.Image;
import project.models.Person;
import project.models.util.entity.ImagePath;
import project.security.TokenProvider;
import project.services.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
public class ApiGeneralController {

    private PostService postService;
    private PersonService personService;
    private GeneralService generalService;
    private TokenProvider tokenProvider;
    private ImagePath imagePath;
    private NotificationService notificationService;
    private PostLikeService postLikeService;

    @GetMapping("liked")
    public ResponseEntity<?> liked(
            @RequestParam(value = "user_id", required = false) Integer userId,
            @RequestParam(value = "item_id") Integer itemId,
            @RequestParam(value = "type") String objectType) {

        if (objectType.equals("Post")) {
            List<Integer> likedPersons = postLikeService.getAllPersonIdWhoLikedPost(itemId);
            return ResponseEntity.ok(new LikedDto(likedPersons.contains(userId)));
        } else {
            return ResponseEntity.ok("null");
        }
    }

    @GetMapping("feeds")
    public ResponseEntity<ListResponseDto<PostDto>> feeds(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage,
            HttpServletRequest servletRequest)
            throws BadRequestException400 {

        Person person = personService.getPersonByToken(servletRequest);
        person.setLastOnlineTime(new Date());
        personService.saveLastOnlineTime(person);

        return ResponseEntity.ok(postService.findAllPosts(name, offset, itemPerPage));
    }

    @GetMapping("notifications")
    public ResponseEntity<?> getAllNotifications(@RequestParam(defaultValue = "0") Integer offset,
                                                 @RequestParam(defaultValue = "20") Integer itemPerPage,
                                                 HttpServletRequest servletRequest) {
        Person person = personService.getPersonByToken(servletRequest);
        return ResponseEntity.ok(
                notificationService.findAllNotificationsByPersonId(person, offset, itemPerPage));
    }

    @PutMapping("notifications")
    public ResponseEntity<?> readNotifications(@RequestParam(required = false) Integer id,
                                               @RequestParam(required = false) Boolean all,
                                               HttpServletRequest servletRequest) {
        Person person = personService.getPersonByToken(servletRequest);
        return ResponseEntity.ok(new ListResponseDto<>((long) 0, 0, 0, new ArrayList<>()));
                //notificationService.findAllNotificationsByPersonId(person, 0, 20));
    }

    @PostMapping("storage")
    ResponseEntity<?> upload(@RequestParam String type,
                             @RequestPart("file") MultipartFile multipartFile,
                             HttpServletRequest request) throws IOException {
        FileUploadResponseDto response = new FileUploadResponseDto();

        if (!type.equals("IMAGE") || multipartFile.isEmpty())
            throw  new BadRequestException400();

        response.setFileName(multipartFile.getOriginalFilename());
        response.setRelativeFilePath("");
        response.setFileFormat(multipartFile.getContentType());
        response.setBytes(multipartFile.getSize());
        response.setFileType("IMAGE");
        response.setCreatedAt(new Date().getTime());

        Person person = personService.getPersonByToken(request);
        response.setOwnerId(person.getId());
        String photo = person.getPhoto();
        if (photo.equals(imagePath.getDefaultImagePath())) {
            Integer id = generalService.saveImage(multipartFile.getBytes(), multipartFile.getContentType());
            response.setId(String.valueOf(id));
            String URL = imagePath.getImagePath()  + id;
            response.setRawFileURL(URL);
            personService.updatePhoto(person, URL);
        } else {
            Integer oldId = Integer.valueOf(photo.replace(imagePath.getImagePath(), ""));
            response.setId(String.valueOf(oldId));
            response.setRawFileURL(imagePath.getImagePath());
            try {
                generalService.updateImage(multipartFile.getBytes(), multipartFile.getContentType(), oldId);
            } catch(EntityNotFoundException e) {
                log.error(e.getMessage(), e);
                throw new BadRequestException400();
            }
        }

        return ResponseEntity.ok(new ResponseDto<>(response));
    }

    @GetMapping(value = "storage/{id}")
    ResponseEntity<?> download(@PathVariable Integer id) {
        Image image;
        try {
            image = generalService.findImage(id);
        } catch(EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException400();
        }

        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(image.getImage());
    }

    @GetMapping(value = "storage/default", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody byte[] downloadDefault() throws IOException {
        Resource resource = new ClassPathResource("images/default-user-image.png");
        return IOUtils.toByteArray(resource.getInputStream());
    }
}
