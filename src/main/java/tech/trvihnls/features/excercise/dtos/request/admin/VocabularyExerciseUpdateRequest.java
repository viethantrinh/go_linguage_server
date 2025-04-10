package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyExerciseUpdateRequest {
    private long exerciseId;
    private long wordId;
}
