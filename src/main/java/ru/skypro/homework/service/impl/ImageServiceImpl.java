package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.logger.FormLogInfo;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ImageEntity uploadImage(MultipartFile imageFile) throws IOException {
        log.info(FormLogInfo.getInfo());
        ImageEntity image = new ImageEntity();
        image.setData(imageFile.getBytes());
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        return imageRepository.save(image);
    }

    @Override
    public ImageEntity getImageById(long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Image with id " + id + " not found!"));
    }

    @Override
    public void remove(ImageEntity image) {
        imageRepository.delete(image);
    }
}
