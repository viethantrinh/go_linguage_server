package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_vocabulary_exercise")
public class VocabularyExercise {

    @EmbeddedId
    private VocabularyExerciseId id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @MapsId("exerciseId")
    @OneToOne
    @JoinColumn(name = "exercise_id", insertable = false, updatable = false, unique = true)
    private Exercise exercise;

    @MapsId("wordId")
    @OneToOne
    @JoinColumn(name = "word_id", insertable = false, updatable = false, unique = true)
    private Word word;
}
