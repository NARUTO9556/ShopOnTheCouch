package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.io.IOException;


public interface CommentService {
//    Comment getAllComment(Long id);
    Comments getAllComments(Long id);

    CreateOrUpdateComment addComment(Long id, CreateOrUpdateComment createOrUpdateComment, String username) throws IOException;

    String deleteComment(Long commentId, String username);

    CreateOrUpdateComment updateComment(Long adId, Long commentId, CreateOrUpdateComment createOrUpdateComment);
}
