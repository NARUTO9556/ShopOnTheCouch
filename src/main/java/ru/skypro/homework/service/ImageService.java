package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;

import java.io.IOException;

public interface ImageService {

    ImageEntity uploadImage(MultipartFile imageFile) throws IOException;


    ImageEntity getImageById(long id);


    void remove(ImageEntity image);

}
