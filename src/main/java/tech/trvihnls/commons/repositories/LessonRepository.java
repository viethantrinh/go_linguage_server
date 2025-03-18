package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("select l from Lesson l where l.topic.id = ?1 order by l.displayOrder asc")
    List<Lesson> findByTopicIdOrderByDisplayOrderAsc(Long id);

    @Query("select l from Lesson l where l.topic.id = ?1 and l.lessonType.id = ?2")
    List<Lesson> findByTopicIdAndLessonTypeId(Long topicId, Long lessonTypeId);


}
