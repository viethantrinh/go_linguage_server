package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogueExerciseCreateRequest {
    private String context;
    private long exerciseId;

    @Builder.Default
    private List<DialogueExerciseLineCreateRequest> dialogueLines = new ArrayList<>();
}
