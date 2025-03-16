package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_exercise")
public class Exercise extends BaseEntity {

    @Column(name = "instruction", length = 256, nullable = false)
    private String instruction;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "exercise_type_id")
    private ExerciseType exerciseType;

    @OneToOne(mappedBy = "exercise")
    private VocabularyExercise vocabularyExercise;

    @OneToOne(mappedBy = "exercise")
    private MultipleChoiceExercise multipleChoiceExercise;

    @OneToOne(mappedBy = "exercise")
    private MatchingExercise matchingExercise;
}
