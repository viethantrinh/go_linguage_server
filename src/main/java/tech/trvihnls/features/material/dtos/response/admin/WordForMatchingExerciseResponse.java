package tech.trvihnls.features.material.dtos.response.admin;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordForMatchingExerciseResponse {
    private long wordId;
    private String englishText;
    private String vietnameseText;
    private String imageUrl;
    private String audioUrl;
}
