package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import tech.trvihnls.commons.utils.enums.MultipleChoiceExerciseEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_multiple_choice_option")
public class MultipleChoiceOption extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "option_type")
    private MultipleChoiceExerciseEnum optionType;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "multiple_choice_exercise_id")
    private MultipleChoiceExercise multipleChoiceExercise;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;
}
