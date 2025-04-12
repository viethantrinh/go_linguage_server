package tech.trvihnls.features.material.dtos.response.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordForVocabularyExerciseResponse {
    private long wordId;
    private String englishText;
    private String vietnameseText;
    private String imageUrl;
    private String audioUrl;

    @JsonProperty("isSelectedByAnotherExercise")
    private boolean isSelectedByAnotherVocabularyExercise;
}
