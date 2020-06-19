package project.services;

import org.springframework.stereotype.Service;
import project.models.VerificationToken;
import project.repositories.VerificationTokenRepository;

import java.util.Optional;

@Service
public class VerificationTokenService {

    private VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public Optional<VerificationToken> findByUUID(String token) {
        return verificationTokenRepository.findByUUID(token);
    }

    public void save(VerificationToken token){
        verificationTokenRepository.save(token);
    }

    public void delete(int id) {
        verificationTokenRepository.deleteById(id);
    }
}