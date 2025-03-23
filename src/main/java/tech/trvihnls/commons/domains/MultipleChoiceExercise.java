package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import tech.trvihnls.commons.utils.enums.LanguageEnum;
import tech.trvihnls.commons.utils.enums.MultipleChoiceExerciseEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_multiple_choice_exercise")
public class MultipleChoiceExercise extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "question_type")
    private MultipleChoiceExerciseEnum questionType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "source_language")
    private LanguageEnum sourceLanguage;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "target_language")
    private LanguageEnum targetLanguage;

    @OneToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @OneToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @OneToOne
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @OneToMany(mappedBy = "multipleChoiceExercise")
    @Builder.Default
    private List<MultipleChoiceOption> multipleChoiceOptions = new ArrayList<>();
}
