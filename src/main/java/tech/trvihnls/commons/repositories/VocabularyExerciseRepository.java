package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.commons.domains.VocabularyExercise;
import tech.trvihnls.commons.domains.VocabularyExerciseId;

import java.util.Set;

@Repository
public interface VocabularyExerciseRepository extends JpaRepository<VocabularyExercise, VocabularyExerciseId> {

    @Query("select ve.word.id from VocabularyExercise ve")
    Set<Long> findAllWordsID();

    @Transactional
    @Modifying
    @Query("delete from VocabularyExercise v where v.exercise = ?1")
    void deleteByExercise(Exercise exercise);


}