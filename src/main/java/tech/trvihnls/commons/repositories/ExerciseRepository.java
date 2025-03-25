package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Exercise;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("select e from Exercise e where e.lesson.id = ?1 order by e.displayOrder asc")
    List<Exercise> findByLessonIdOrderByDisplayOrderAsc(Long id);

    @Query("select e from Exercise e where e.lesson.id = ?1")
    List<Exercise> findByLessonId(Long id);

    @Query("select e from Exercise e where e.lesson.id = ?1 and e.exerciseType.id = ?2")
    List<Exercise> findByLessonIdAndExerciseTypeId(Long lessonId, Long exerciseTypeId);
}
