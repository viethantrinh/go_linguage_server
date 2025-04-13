package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.WordArrangementExercise;

@Repository
public interface WordArrangementExerciseRepository extends JpaRepository<WordArrangementExercise, Long> {
}