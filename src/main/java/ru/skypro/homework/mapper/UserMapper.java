package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;

/**
 * UserMapper
 * <br><i>содержит методы:</i>
 *
 * <br>- toEntity <i>(из {@link User} в  {@link UserEntity})</i>;
 * <br>- toDtoUser <i>(из {@link UserEntity} в  {@link User})</i>;
 * <br>- toDtoUpdateUser <i>(из {@link UserEntity} в  {@link UpdateUser})</i>;
 */
@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)//
public interface UserMapper {

    //    @Mapping(ignore = true, target = "image.filePath")
    UserEntity toEntity(User dto);

    UpdateUser toDtoUpdateUser(UserEntity userEntity);

    User toDtoUser(UserEntity userEntity);
}
