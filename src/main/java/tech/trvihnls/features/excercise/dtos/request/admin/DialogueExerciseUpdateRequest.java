package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogueExerciseUpdateRequest {
    private String context;
    private long exerciseId;

    @Builder.Default
    private List<DialogueExerciseLineUpdateRequest> dialogueLines = new ArrayList<>();
}
