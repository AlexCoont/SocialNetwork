package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
}
