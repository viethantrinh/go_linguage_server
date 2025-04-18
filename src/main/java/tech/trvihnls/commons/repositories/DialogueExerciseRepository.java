package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.DialogueExercise;

import java.util.Optional;

@Repository
public interface DialogueExerciseRepository extends JpaRepository<DialogueExercise, Long> {
    Optional<DialogueExercise> findByExerciseIdOrderByIdDesc(Long exerciseId);
}
