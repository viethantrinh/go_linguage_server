package tech.trvihnls.mobileapis.main.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"context", "lines"})
public class DialogueResponse {
    private String context;

    @Builder.Default
    private List<DialogueLineResponse> dialogueExerciseLines = new ArrayList<>();
}
