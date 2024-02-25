package ru.skypro.homework.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.skypro.homework.security.logger.FormLogInfo;

@Slf4j
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Комментарий не найден")
public class CommentNotFound extends RuntimeException {
    public CommentNotFound() {
        log.info(FormLogInfo.getException());
    }

    public CommentNotFound(String message) {
        super(message);
    }
}
