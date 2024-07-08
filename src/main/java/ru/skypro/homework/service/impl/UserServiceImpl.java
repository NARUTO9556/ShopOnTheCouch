package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.PasswordIsNotMatchException;
import ru.skypro.homework.exception.UserNotFound;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.logger.FormLogInfo;
import ru.skypro.homework.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис пользователей
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${image.user.dir.path}")
    private String avatarsDir;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    /**
     * получить {@link User} из данных {@link Authentication}
     *
     * @param authentication
     * @return {@link User}
     */
    @Override
    @Transactional
    public User getUser(Authentication authentication) {
        log.info("Получить данные пользователя" + FormLogInfo.getInfo());
        String nameEmail = authentication.getName();
        UserEntity userEntity = findEntityByEmail(nameEmail);
        User user = userMapper.toDtoUser(userEntity);
        if (userEntity.getImage() != null) {
            //именно тут "отдаем" значение!!!! начало см. строка 139
            user.setImage("/users/" + userEntity.getId() + "/avatarsDir");//test////works
//            user.setImage("/users/" + userEntity.getId() + "/avatarsDir");//test
        }
        return user;
    }

    /**
     * Обновить данные пользователя
     */
    @Override
    @Transactional
    public UpdateUser updateUser(UpdateUser newpUpdateUserDto, Authentication authentication) {
        log.info("Обновить данные пользователя:  " + FormLogInfo.getInfo());
        String nameEmail = authentication.getName();
        UserEntity userEntity = findEntityByEmail(nameEmail);
        long id = userEntity.getId();

        UserEntity oldUser = findById(id);

        oldUser.setFirstName(newpUpdateUserDto.getFirstName());
        oldUser.setLastName(newpUpdateUserDto.getLastName());
        oldUser.setPhone(newpUpdateUserDto.getPhone());

        userRepository.save(oldUser);
        log.info("Пользователь успешно обновлен в БД:  " + FormLogInfo.getInfo());

        return userMapper.toDtoUpdateUser(oldUser);
    }

    /**
     * Установить пароль пользователя
     */
    @Override
    @Transactional
    public void setPassword(NewPassword newPass, Authentication authentication) {
        log.info("вызов setPassword :" + FormLogInfo.getInfo());
        String oldPassword = newPass.getCurrentPassword();
        String encodeNewPassword = encoder.encode(newPass.getNewPassword());
        UserEntity userEntity = findEntityByEmail(authentication.getName());
        //проверяем совпадают ли старый пароль, введенный пользователем, и пароль сохраненный в БД
        if (!encoder.matches(oldPassword, userEntity.getPassword()))
            throw new PasswordIsNotMatchException();
        else
            userEntity.setPassword(encodeNewPassword);
        userRepository.save(userEntity);
    }

    /**
     * найти пользователя по email - логину
     *
     * @param email email - логину пользователя
     * @return пользователь
     */
    @Override
    public UserEntity findEntityByEmail(String email) {
        log.info("Пользователь найден по email: " + FormLogInfo.getInfo());
        return userRepository.findByEmail(email).orElseThrow(UserNotFound::new);
    }

    /**
     * найти пользователя по id
     *
     * @param id id пользователя
     * @return пользователь
     */
    public UserEntity findById(long id) {
        log.info("Пользователь найден по Id: " + FormLogInfo.getInfo());
        return userRepository.findById(id).orElseThrow(UserNotFound::new);
    }

    @Override
    @Transactional
    public void updateUserImage(MultipartFile image, Authentication authentication) {
        String login = authentication.getName();
        UserEntity userEntity = findEntityByEmail(login);

        String linkToGetAvatar = userEntity.getId() + "/" + avatarsDir;
//linkToGetAvatar   именно тут и создается ссылка на фото!!!но передача ее происходит в строке 61

        Path path = Path.of(avatarsDir, Objects.requireNonNull(String.valueOf(userEntity.getId())));
        if (userEntity.getImage() != null) {
            try {
                Files.deleteIfExists(path);
                userEntity.setImage(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(path, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);

            userEntity.setImage(linkToGetAvatar);
            userRepository.save(userEntity);
        } catch (Exception e) {
            log.info("Ошибка сохранения файла" + FormLogInfo.getCatch());
        }
    }

    @Override
    public byte[] getAvatarById(Long id) {
        String linkToGetAvatar = avatarsDir + id;
//        System.out.println("linkToGetAvatar = " + linkToGetAvatar);//для сверки значения
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(linkToGetAvatar));
        } catch (IOException e) {
            log.info(FormLogInfo.getCatch());
            throw new RuntimeException(e);
        }
        return bytes;
    }
}