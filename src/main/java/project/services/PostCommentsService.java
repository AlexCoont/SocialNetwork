package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.CommentDto;
import project.dto.CommentModelDto;
import project.models.Person;
import project.models.PostComment;
import project.repositories.PostCommentsRepository;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostCommentsService {

    private PostCommentsRepository postCommentsRepository;

    @Autowired
    public PostCommentsService(PostCommentsRepository postCommentsRepository) {
        this.postCommentsRepository = postCommentsRepository;
    }

    public List<PostComment> getListComments(Integer postId){
        return postCommentsRepository.findAllByPostIdAndIsBlocked(postId, false);
//        return postComments.stream().map(comment -> new CommentDto(1, comment.getComment(),
//                comment.getId(), comment.getPostId(), comment.getTime(),
//                comment.getAuthorId(), comment.getIsBlocked())).collect(toList());
    }

    public PostComment addNewCommentToPost(Integer postId, CommentModelDto commentModelDto, Person author){

        PostComment postComment = new PostComment();
        postComment.setPostId(postId);
        postComment.setComment(commentModelDto.getCommentText());
        postComment.setAuthor(author);
        postComment.setIsBlocked(false);
        postComment.setTime(new Date());
        postComment.setParentId(postId);
        postComment.setMyLike(false);
        postCommentsRepository.save(postComment);

        return postComment;
    }
}
