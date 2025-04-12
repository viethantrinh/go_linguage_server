package tech.trvihnls.features.excercise.dtos.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceExerciseDetailResponse {
    private long exerciseId;

    private String exerciseName;

    private String questionType;

    private String sourceLanguage;

    private String targetLanguage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long relatedWordId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long relatedSentenceId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MultipleChoiceOptionDetailResponse> options = new ArrayList<>();
}
