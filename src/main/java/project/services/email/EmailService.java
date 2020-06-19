package project.services.email;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void send(String mailTo, String subject, String message);

}
