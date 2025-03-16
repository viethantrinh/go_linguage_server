package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_word_arrangement_option")
public class WordArrangementOption extends BaseEntity {

    @Column(name = "word_text", nullable = false)
    private String wordText;

    @Column(name = "is_distractor", nullable = false)
    private boolean isDistractor;

    @Column(name = "correct_position", nullable = false)
    private int correctPosition;

    @ManyToOne
    @JoinColumn(name = "word_arrangement_exercise_id")
    private WordArrangementExercise wordArrangementExercise;
}
