package project.services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import project.dto.PostDto;
import project.dto.requestDto.PostRequestBodyTagsDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.*;
import project.models.enums.NotificationTypeEnum;
import project.models.enums.PostTypeEnum;
import project.repositories.NotificationRepository;
import project.repositories.NotificationTypeRepository;
import project.repositories.PostRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private TagService tagService;

    private PostLikeService postLikeService;
    private PersonService personService;
    private PostCommentsService postCommentsService;
    private NotificationTypeRepository notificationTypeRepository;
    private FriendshipService friendshipService;
    private NotificationRepository notificationRepository;

    public ListResponseDto<PostDto> findAllPosts(String name, Integer offset, Integer itemPerPage)
            throws BadRequestException400 {
        Sort sort = Sort.by(Sort.Direction.DESC, name == null ? "time" : "title");
        Pageable pageable = PageRequest.of(offset, itemPerPage, sort);
        List<Post> postList = name != null ?
                postRepository.findAllByTitleContainingAndTimeBeforeAndIsBlocked(
                        name, new Date(), false, pageable)
                :
                postRepository.findAllByTimeBeforeAndIsBlocked(new Date(), false, pageable);
        if (postList == null) throw new BadRequestException400();
        List<PostDto> postDtoList = postList.stream().map(post -> getPostDtoById(null, post)).collect(toList());

        return new ListResponseDto<>((long) postDtoList.size(), offset, itemPerPage, postDtoList);
    }

    public Post getPostById(Integer id) throws BadRequestException400 {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (!optionalPost.isPresent()) throw new BadRequestException400();
        return optionalPost.orElse(null);
    }

    public ResponseDto<PostDto> editPostById(Integer id, Long publishDate, PostRequestBodyTagsDto dto)
            throws BadRequestException400 {
        Post post = getPostById(id);
        if (post == null) throw new BadRequestException400();
        post.setTitle(dto.getTitle());
        post.setPostText(dto.getPostText());
        Post postDB = postRepository.save(post);

        saveTags(dto.getTags(), postDB);

        return new ResponseDto<>(getPostDtoById(null, postDB));
    }

    public ResponseDto<Integer> deletePostById(@PathVariable Integer id) {
        postRepository.deleteById(id);
        return new ResponseDto<>(id);
    }

    @SneakyThrows
    public PostDto getPostDtoById(Integer id, Post post2Dto) {
        Post post = post2Dto == null ? getPostById(id) : post2Dto;
        if (post == null) throw new BadRequestException400();

        Integer countLikes = postLikeService.countLikesByPostId(post.getId());

        List<PostComment> comments = postCommentsService.getListComments(post.getId());

        List<String> tags = post.getTagList().stream().map(Tag::getTag).collect(toList());

        return new PostDto(post.getId(), post.getTime(), post.getAuthor(), post.getTitle(),
                post.getPostText(), post.getIsBlocked(), countLikes, post.getMyLike(), comments, tags,
                post.getTime().before(new Date()) ?
                                PostTypeEnum.POSTED.getType()
                                :
                                PostTypeEnum.QUEUED.getType());
    }


    @Transactional
    public ResponseDto<PostDto> addNewWallPostByAuthorId(Integer authorId,
                                                         Long publishDate,
                                                         PostRequestBodyTagsDto dto) throws BadRequestException400 {
        Post post = new Post();
        Person author = personService.findPersonById(authorId);
        Date publishTime = publishDate == null ? new Date() : getDateFromLong(publishDate + "");
        post.setAuthor(author);
        post.setTime(publishTime);
        post.setTitle(dto.getTitle());
        post.setPostText(dto.getPostText());
        post.setIsBlocked(false);
        post.setMyLike(false);
        Post finalPost = postRepository.save(post);

        if (publishTime.before(new Date())) {
           List<Person> friendList = friendshipService.getFriendsList(author);
            friendList.forEach(friend -> {

                Notification notification = new Notification();
                NotificationType notificationType = notificationTypeRepository.findByCode(NotificationTypeEnum.POST);
                notification.setPerson(friend);
                notification.setContact("Contact");
                notification.setMainEntity(finalPost);
                notification.setNotificationType(notificationType);
                notification.setSentTime(new Date());
                notificationRepository.save(notification);
            });
        }

        saveTags(dto.getTags(), finalPost);

        return new ResponseDto<>(getPostDtoById(null, finalPost));
    }

    private void saveTags(List<String> tags, Post post) {
        if (tags.size() > 0) {
            tags.forEach(tag -> {
                Tag tag2DB = tagService.findByTagName(tag);
                if (tag2DB == null) {
                    tag2DB = tagService.saveTag(tag);
                }

                if (!post.getTagList().contains(tag2DB)) {
//                    Post2Tag post2Tag = new Post2Tag();
//                    post2Tag.setPostId(post.getId());
//                    post2Tag.setTag(tag2DB.getId());
//                    post2TagService.addNewPost2Tag(post2Tag);
                    post.getTagList().add(tag2DB);
                    postRepository.save(post);
                }
            });
        }
    }

    public ListResponseDto findAllByAuthorId(
            Integer authorId, Integer offset, Integer limit, Integer compareId)
            throws BadRequestException400 {
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        List<Post> wallPostList = !authorId.equals(compareId) ?
                postRepository.findAllByAuthorIdAndTimeBeforeAndIsBlocked(
                        authorId, new Date(), false, pageable)
                :
                postRepository.findAllByAuthorIdAndIsBlocked(authorId, false, pageable);
        if (wallPostList == null) throw new BadRequestException400();

        List<PostDto> personsWallPostDtoList = wallPostList.stream()
                .map(post -> getPostDtoById(null, post))
                .collect(Collectors.toList());

        return new ListResponseDto<>((long) personsWallPostDtoList.size(), offset, limit, personsWallPostDtoList);
    }

    @SneakyThrows
    public List<Post> getPostsByTitleAndDate(
            String title, String dateFrom, String dateTo, Integer offset, Integer limit) {  //как тут обрабоать ошибку я не понял)
        Pageable pageable = PageRequest.of(offset, limit);

        Date startDate = getDateFromLong(dateFrom);
        Date endDate = getDateFromLong(dateTo);

        if (!title.isEmpty() && startDate != null && endDate != null) {
            return postRepository.findAllByTitleContainingAndTimeBetweenAndIsBlocked(title, startDate,
                    endDate, false, pageable);
        }

        if (!title.isEmpty() && startDate != null) {
            return postRepository
                    .findAllByTitleContainingAndTimeAfterAndIsBlocked(title, startDate, false, pageable);
        }

        if (!title.isEmpty() && endDate != null) {
            return postRepository
                    .findAllByTitleContainingAndTimeBeforeAndIsBlocked(title, endDate, false, pageable);
        }

        if (!title.isEmpty()) {
            return postRepository.findAllByTitleContainingAndIsBlocked(title, false, pageable);
        }

        if (startDate != null && endDate != null) {
            return postRepository.findAllByTimeBetweenAndIsBlocked(startDate, endDate, false, pageable);
        }

        if (startDate != null) {
            return postRepository.findAllByTimeAfterAndIsBlocked(startDate, false, pageable);
        }

        if (endDate != null) {
            return postRepository.findAllByTimeBeforeAndIsBlocked(endDate, false, pageable);
        }

        return postRepository.findAllByIsBlocked(false, pageable);
    }

    public void deleteAllPostsByAuthorId(Integer id) {
        postRepository.deleteAllByAuthorId(id);
    }

    public Date getDateFromLong(String date) {
        if (!date.isEmpty() && !date.equals("null")) {
            Calendar calendar = Calendar.getInstance();
            long dateLong = Long.parseLong(date);
            calendar.setTimeInMillis(dateLong);
            return calendar.getTime();
        }
        return null;
    }

    public void setMyLike(Integer postId, Boolean isLiked) {
        Post post = getPostById(postId);
        post.setMyLike(isLiked);
        postRepository.save(post);
    }
}
