package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.DialogueExerciseLine;

@Repository
public interface DialogueExerciseLineRepository extends JpaRepository<DialogueExerciseLine, Long> {
}
