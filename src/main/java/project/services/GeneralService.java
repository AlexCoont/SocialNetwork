package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.handlerExceptions.EntityNotFoundException;
import project.models.Image;
import project.repositories.ImageRepository;

@Service
@AllArgsConstructor
public class GeneralService {

    private ImageRepository imageRepository;

    public Integer saveImage(byte[] arrayImage, String type) {
        Image image = new Image();
        image.setImage(arrayImage);
        image.setType(type);
        Image imageSaved = imageRepository.save(image);
        return imageSaved.getId();
    }

    public Image findImage(Integer id) throws EntityNotFoundException {
        return imageRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Can't find entity with id: " + id)
        );
    }

    public void deleteImage(Integer id) {
        imageRepository.deleteById(id);
    }

    public void updateImage(byte[] arrayImage, String type, Integer id) throws EntityNotFoundException {
        Image image = findImage(id);
        image.setImage(arrayImage);
        image.setType(type);
        imageRepository.save(image);
    }
}
