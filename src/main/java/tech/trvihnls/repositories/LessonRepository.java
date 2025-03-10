package tech.trvihnls.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.models.entities.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("select l from Lesson l where l.topic.id = ?1 order by l.displayOrder")
    List<Lesson> findByTopicIdOrderByDisplayOrderAsc(Long id);
}
