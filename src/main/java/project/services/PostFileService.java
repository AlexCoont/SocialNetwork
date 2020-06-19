package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.repositories.PostFileRepository;

@Service
public class PostFileService {

    private PostFileRepository postFileRepository;

    @Autowired
    public PostFileService(PostFileRepository postFileRepository) {
        this.postFileRepository = postFileRepository;
    }
}
