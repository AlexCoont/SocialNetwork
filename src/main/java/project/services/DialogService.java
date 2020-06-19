package project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.models.Dialog;
import project.repositories.DialogRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DialogService {
    private DialogRepository dialogRepository;


}
