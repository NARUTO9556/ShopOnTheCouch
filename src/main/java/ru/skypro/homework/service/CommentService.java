package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;

import java.io.IOException;


public interface CommentService {
//    Comment getAllComment(Long id);
    Comments getAllComments(Long id);

    CreateOrUpdateComment addComment(Long id, CreateOrUpdateComment createOrUpdateComment, String username) throws IOException;

    String deleteComment(Long commentId, String username);

    CreateOrUpdateComment updateComment(Long adId, Long commentId, CreateOrUpdateComment createOrUpdateComment);
}
