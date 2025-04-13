package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordArrangementExerciseCreateRequest {
    private long exerciseId;
    private long sentenceId;
    private String sourceLanguage;
    private String targetLanguage;
    private List<WordArrangementOptionCreateRequest> options;
}