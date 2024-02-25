package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;

import java.io.IOException;


public interface CommentService {
    Comment getAllComment(Long id);
//    Collection<Comment> getCommentToMe(Authentication authentication);

    CreateOrUpdateComment addComment(AdEntity id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) throws IOException;

    String deleteComment(Long commentId, Authentication userName);

    CreateOrUpdateComment updateComment(Long commentId, CreateOrUpdateComment createOrUpdateComment, Authentication userName);
}
