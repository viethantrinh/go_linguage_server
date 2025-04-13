package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.MatchingExercise;

import java.util.Optional;

@Repository
public interface MatchingExerciseRepository extends JpaRepository<MatchingExercise, Long> {
  @Query("select m from MatchingExercise m where m.exercise.id = ?1")
  Optional<MatchingExercise> findByExerciseId(Long id);

}
