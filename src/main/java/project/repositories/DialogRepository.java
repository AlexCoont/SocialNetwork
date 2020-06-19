package project.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import project.models.Dialog;

import java.util.List;

public interface DialogRepository extends CrudRepository<Dialog, Integer> {

        List<Dialog> findAll(Pageable pageable);
}
