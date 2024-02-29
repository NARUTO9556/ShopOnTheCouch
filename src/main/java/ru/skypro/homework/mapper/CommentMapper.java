package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * CommentMapper
 * <br><i>содержит методы:</i>
 * <br>-toEntity<i></i>
 * <br>-toCommentDto<i></i>
 * <br>-toCommentsDto<i></i>
 * <br>-toCreateOrUpdateCommentDto<i></i>
 */
@Mapper(componentModel = "string")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    // to use CommentMapper.INSTANCE.toEntity(---);
    // to use CommentMapper.INSTANCE.toDto___();

    //_____ toEntity___
    @Mapping(source = "author", target = "id")
    @Mapping(source = "pk", target = "pk.id")
    @Mapping(source = "authorImage", target = "author.image.id")
    @Mapping(source = "authorFirstName", target = "author.firstName")
    @Mapping(source = "createdAt", target = "createdAt")
    CommentEntity toEntity(Comment dto);

//    CommentEntity toEntity(Comments dto);
    @Mapping(source = "text", target = "text")
    CommentEntity toEntity(CreateOrUpdateComment dto);

    //_____ toDto___
    @Mapping(target = "author", source = "id")
    @Mapping(target = "pk", source = "pk.id")
    @Mapping(target = "authorImage", source = "author.image.id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "createdAt", source = "createdAt")
    Comment toCommentDto(CommentEntity commentEntity);

//    Comments toCommentsDto(CommentEntity entity);
    @Mapping(target = "text", source = "text")
    CreateOrUpdateComment toCreateOrUpdateCommentDto(CommentEntity commentEntity);
    @Mapping(target = "count", source = "size")
    @Mapping(target = "results", source = "list")
    Comments toComments(Integer size, List<CommentEntity> list);

}
