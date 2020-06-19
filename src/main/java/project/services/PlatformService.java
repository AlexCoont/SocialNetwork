package project.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.models.Language;
import project.repositories.LanguageRepository;

@Service
@AllArgsConstructor
public class PlatformService {

    private LanguageRepository languageRepository;

    public Page<Language> getLanguages(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return languageRepository.findAll(pageable);
    }

    public Page<Language> getLanguages(String language, Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return languageRepository.findByLanguageContaining(language, pageable);
    }
}