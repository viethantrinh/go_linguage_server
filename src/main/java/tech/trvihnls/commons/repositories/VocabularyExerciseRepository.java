package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.VocabularyExercise;
import tech.trvihnls.commons.domains.VocabularyExerciseId;

@Repository
public interface VocabularyExerciseRepository extends JpaRepository<VocabularyExercise, VocabularyExerciseId> {
}