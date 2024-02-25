package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

/**
 * Репозиторий для комментариев
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query(value = "select * from comments where ad_id = :ad_id", nativeQuery = true)
    CommentEntity findByAdId(@Param("ad_id") Long id);
}
