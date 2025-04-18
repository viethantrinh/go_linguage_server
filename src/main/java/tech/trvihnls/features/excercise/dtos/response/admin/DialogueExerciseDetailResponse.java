package tech.trvihnls.features.excercise.dtos.response.admin;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogueExerciseDetailResponse {
    private long id;
    private String context;
    private long exerciseId;
    @Builder.Default
    private List<DialogueExerciseDetailLineResponse> dialogueLines = new ArrayList<>();
}
