package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Integer> {}
