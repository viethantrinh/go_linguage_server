package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.MultipleChoiceExercise;

@Repository
public interface MultipleChoiceExerciseRepository extends JpaRepository<MultipleChoiceExercise, Long> {
}