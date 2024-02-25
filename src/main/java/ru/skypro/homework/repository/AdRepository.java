package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;

import java.util.Collection;
import java.util.List;

/**
 * Репозиторий для объявлений
 */
@Repository
public interface AdRepository extends JpaRepository<AdEntity,Long> {
    List<AdEntity> findAllByAuthorId(int id);
}
