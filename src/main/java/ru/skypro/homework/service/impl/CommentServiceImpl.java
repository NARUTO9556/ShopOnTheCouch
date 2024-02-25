package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.logger.FormLogInfo;
import ru.skypro.homework.service.CommentService;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userService;

    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              AdMapper adMapper,
                              UserMapper userMapper,
                              CommentMapper commentMapper,
                              UserServiceImpl userService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }
    @Transactional
    @Override
    public Comment getAllComment(Long id) {
        return commentMapper.toCommentDto(commentRepository.findByAdId(id));
    }

//    @Override
//    public Collection<Comment> getCommentToMe(Authentication authentication) {
//        return null;
//    }

    @Override
    public CreateOrUpdateComment addComment(AdEntity id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) throws IOException {
        log.info(FormLogInfo.getInfo());
        CommentEntity commentEntity = commentMapper.toEntity(createOrUpdateComment);
        User user = userService.getUser(authentication);
        commentEntity.setAuthor(userMapper.toEntity(user));
        commentEntity.setText(String.valueOf(createOrUpdateComment));
        commentEntity.setPk(id);
        commentRepository.save(commentEntity);
        return commentMapper.toCreateOrUpdateCommentDto(commentEntity);
    }
    @Transactional
    @Override
    public String deleteComment(Long commentId, Authentication userName) {
        return null;
    }
    @Transactional
    @Override
    public CreateOrUpdateComment updateComment(Long commentId, CreateOrUpdateComment createOrUpdateComment, Authentication userName) {
        return null;
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private UserEntity getUser() {
        UserEntity user = null;
        try {
            user = userRepository.findByEmail(getCurrentUser()).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
        return user;
    }
}
