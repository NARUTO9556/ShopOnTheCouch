package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    /**
     * id автора комментария
     */
    @Schema(description = "id автора комментария")
    private Integer author;
    /**
     * ссылка на аватар автора комментария
     */
    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;
    /**
     * имя создателя комментария
     */
    @Schema(description = "имя создателя комментария")
    private String authorFirstName;
    /**
     * дата и время создания комментария в милисекундах с 00:00:00 01.01.1970
     */
    @Schema(description = "дата и время создания комментария в милисекундах с 00:00:00 01.01.1970")
    private LocalDateTime createdAt;
    /**
     * id комментария
     */
    @Schema(description = "id комментария")
    private Integer pk;
    /**
     * текст комментария
     */
    @Schema(description = "текст комментария")
    private String text;
}
