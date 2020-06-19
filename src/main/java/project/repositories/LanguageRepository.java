package project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.models.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    Page<Language> findByLanguageContaining(String language, Pageable pageable);
}