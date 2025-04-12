package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Sentence;
import tech.trvihnls.features.material.dtos.query.SentenceQuery;

import java.util.List;
import java.util.Set;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    @Query(value = """
                SELECT
                    s.id as id,
                    s.english_text as english_text,
                    s.vietnamese_text as vietnamese_text,
                    s.audio_url as audio_url
                FROM
                    tbl_sentence s
                    INNER JOIN tbl_topic_sentence ts ON ts.sentence_id = s.id
                    INNER JOIN tbl_topic t ON t.id = ts.topic_id
                    INNER JOIN tbl_lesson l ON l.topic_id = t.id
                    INNER JOIN tbl_exercise e ON e.lesson_id = l.id
                WHERE
                    e.id = ?1
            """,
            nativeQuery = true)
    List<SentenceQuery> findByExerciseId(Long exerciseId);
}
