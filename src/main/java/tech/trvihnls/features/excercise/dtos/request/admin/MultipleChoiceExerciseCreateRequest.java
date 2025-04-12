package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceExerciseCreateRequest {
    private long exerciseId;
    private String questionType;
    private String sourceLanguage;
    private String targetLanguage;
    private Long wordId;
    private Long sentenceId;
    private List<MultipleChoiceOptionCreateRequest> options;
}
