package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Word;
import tech.trvihnls.features.material.dtos.query.WordQuery;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = """
                SELECT
                    w.id as id,
                    w.english_text as english_text,
                    w.vietnamese_text as vietnamese_text,
                    w.audio_url as audio_url,
                    w.image_url as image_url
                FROM
                    tbl_word w
                    INNER JOIN tbl_topic_word tw ON tw.word_id = w.id
                    INNER JOIN tbl_topic t ON t.id = tw.topic_id
                    INNER JOIN tbl_lesson l ON l.topic_id = t.id
                    INNER JOIN tbl_exercise e ON e.lesson_id = l.id
                WHERE
                    e.id = ?1
            """,
            nativeQuery = true)
    List<WordQuery> findByExerciseId(Long exerciseId);

}
