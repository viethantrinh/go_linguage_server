package tech.trvihnls.commons.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VocabularyExerciseId implements Serializable {

    @Column(name = "word_id")
    private Long wordId;

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        VocabularyExerciseId that = (VocabularyExerciseId) o;
        return Objects.equals(wordId, that.wordId) && Objects.equals(exerciseId, that.exerciseId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(wordId);
        result = 31 * result + Objects.hashCode(exerciseId);
        return result;
    }
}
