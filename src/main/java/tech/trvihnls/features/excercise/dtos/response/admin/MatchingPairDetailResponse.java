// src/main/java/tech/trvihnls/features/excercise/dtos/response/admin/MatchingPairDetailResponse.java
package tech.trvihnls.features.excercise.dtos.response.admin;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPairDetailResponse {
    private long id;
    private long matchingExerciseId;
    private WordDetail word;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WordDetail {
        private long wordId;
        private String englishText;
        private String vietnameseText;
        private String imageUrl;
        private String audioUrl;
    }
}
