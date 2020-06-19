package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.VerificationToken;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Integer> {

    Optional<VerificationToken> findByUUID(String token);
}