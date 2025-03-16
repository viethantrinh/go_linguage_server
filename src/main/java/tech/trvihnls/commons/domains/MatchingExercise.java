package tech.trvihnls.commons.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_matching_exercise")
public class MatchingExercise extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Builder.Default
    @OneToMany(mappedBy = "matchingExercise")
    private List<MatchingPair> matchingPairs = new ArrayList<>();
}
