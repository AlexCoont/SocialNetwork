package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.BlockHistoryRepository;

@Service
@AllArgsConstructor
public class BlockHistoryService {
    private BlockHistoryRepository blockHistoryRepository;
}
