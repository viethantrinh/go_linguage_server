package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.MultipleChoiceExercise;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MultipleChoiceExerciseRepository extends JpaRepository<MultipleChoiceExercise, Long> {
    @Query("select m from MultipleChoiceExercise m where m.exercise.id = ?1")
    Optional<MultipleChoiceExercise> findByExerciseId(Long id);

    @Query("select mce.word.id from MultipleChoiceExercise mce where mce.word.id is not null")
    Set<Long> findAllWordsID();

    @Query("select mce.sentence.id from MultipleChoiceExercise mce where mce.sentence.id is not null")
    Set<Long> findAllSentencesID();
}
