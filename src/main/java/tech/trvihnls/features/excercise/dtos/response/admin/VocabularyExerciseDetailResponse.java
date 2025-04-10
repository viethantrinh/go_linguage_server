package tech.trvihnls.features.excercise.dtos.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyExerciseDetailResponse {
    private long exerciseId;
    private String exerciseName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long relatedWordId;
}
