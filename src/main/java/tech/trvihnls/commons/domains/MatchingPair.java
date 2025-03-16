package tech.trvihnls.commons.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_matching_pair")
public class MatchingPair extends  BaseEntity {

    @ManyToOne
    @JoinColumn(name = "matching_exercise_id")
    private MatchingExercise matchingExercise;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;
}
