package ru.skypro.homework.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.skypro.homework.security.logger.FormLogInfo;

@Slf4j
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Фото не найдено.")
public class ImageNotFound extends RuntimeException{
    public ImageNotFound() {
        log.info(FormLogInfo.getException());
    }
}
