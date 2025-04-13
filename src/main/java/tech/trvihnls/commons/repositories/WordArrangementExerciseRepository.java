package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.WordArrangementExercise;

import java.util.Optional;
import java.util.Set;

@Repository
public interface WordArrangementExerciseRepository extends JpaRepository<WordArrangementExercise, Long> {
    @Query("select w from WordArrangementExercise w where w.exercise.id = ?1")
    Optional<WordArrangementExercise> findByExerciseId(Long id);

    @Query("select wae.sentence.id from WordArrangementExercise wae")
    Set<Long> findAllSentencesID();
}