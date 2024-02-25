package ru.skypro.homework.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skypro.homework.mapper.CommentMapper;

@Configuration
public class MapperBean {
    @Bean
    public CommentMapper commentMapper() {
        return Mappers.getMapper(CommentMapper.class);
    }
}
