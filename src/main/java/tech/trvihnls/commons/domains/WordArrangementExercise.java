package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import tech.trvihnls.commons.utils.enums.LanguageEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_word_arrangement_exercise")
public class WordArrangementExercise extends BaseEntity {

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
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @Builder.Default
    @OneToMany(mappedBy = "wordArrangementExercise")
    private List<WordArrangementOption> wordArrangementOptions = new ArrayList<>();
}
