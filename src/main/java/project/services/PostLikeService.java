package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.models.Person;
import project.models.PostLike;
import project.repositories.PostLikeRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostLikeService {

    private PostLikeRepository postLikeRepository;
    private PersonService personService;

    @Autowired
    public PostLikeService(PostLikeRepository postLikeRepository, PersonService personService) {
        this.postLikeRepository = postLikeRepository;
        this.personService = personService;
    }

    public Integer countLikesByPostId (Integer postId){
        return postLikeRepository.countAllByPostId(postId);
    }

    public PostLike addLike(Integer personId, Integer postId){
        PostLike postLike = new PostLike();
        postLike.setPersonId(personId);
        postLike.setPostId(postId);
        postLike.setTime(new Date());
        return postLikeRepository.save(postLike);
    }

    public Integer deleteLike(Integer postId, Integer personId){

        postLikeRepository.deleteByPostIdAndPersonId(postId, personId);

        return postLikeRepository.countAllByPostId(postId);
    }

    public List<Integer> getAllPersonIdWhoLikedPost(Integer postId){

       List<PostLike> postLikeList = postLikeRepository.findAllByPostId(postId);

       return postLikeList.stream().map(PostLike::getPersonId).collect(Collectors.toList());
    }
}
