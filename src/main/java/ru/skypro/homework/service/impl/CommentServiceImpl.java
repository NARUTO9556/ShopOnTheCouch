package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.logger.FormLogInfo;
import ru.skypro.homework.service.CommentService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userService;

    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              AdRepository adRepository,
                              AdMapper adMapper,
                              UserMapper userMapper,
                              CommentMapper commentMapper,
                              UserServiceImpl userService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }
    @Transactional
    @Override
    public Comments getAllComments(Long id) {
        log.info(FormLogInfo.getInfo());
        List<CommentEntity> comments = commentRepository.findByAdId(id);
        return commentMapper.toComments(comments.size(), comments);
    }
    @Transactional
    @Override
    public CreateOrUpdateComment addComment(Long id, CreateOrUpdateComment createOrUpdateComment, String username) throws IOException {
        log.info(FormLogInfo.getInfo());
        CommentEntity commentEntity = commentMapper.toEntity(createOrUpdateComment);
        UserEntity user = userService.findEntityByEmail(username);
        AdEntity ad = adRepository.findById(id).orElse(null);
        commentEntity.setAuthor(user);
        commentEntity.setText(String.valueOf(createOrUpdateComment));
        commentEntity.setPk(ad);
        commentRepository.save(commentEntity);
        return commentMapper.toCreateOrUpdateCommentDto(commentEntity);
    }

    @Override
    public String deleteComment(Long commentId, String username) {
        log.info(FormLogInfo.getInfo());
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            UserEntity author = userService.findEntityByEmail(username);
            if (author.getRole().equals(Role.ADMIN)) {
                commentRepository.delete(comment.get());
                return "комментарий удален";
            } else if (author.getRole().equals(Role.USER)) {
                if (comment.get().getAuthor().getEmail().equals(author.getEmail())) {
                    commentRepository.delete(comment.get());
                    return "комментарий удален";
                } else {
                    return "forbidden"; //'403' For the user deletion is forbidden
                }
            }
        }
        return "not found";
    }
    @Transactional
    @Override
    public CreateOrUpdateComment updateComment(Long adId, Long commentId, CreateOrUpdateComment createOrUpdateComment) {
        CommentEntity comment = commentRepository.findById(commentId).get();
        comment.setText(createOrUpdateComment.getText());
        commentRepository.save(comment);
        return commentMapper.toCreateOrUpdateCommentDto(comment);
    }
}
