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
@Table(name = "tbl_dialogue_exercise")
public class DialogueExercise extends BaseEntity {

    @Column(name = "context", nullable = false)
    private String context;

    @OneToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Builder.Default
    @OneToMany(mappedBy = "dialogueExercise")
    private List<DialogueExerciseLine> dialogueExerciseLines = new ArrayList<>();

}
