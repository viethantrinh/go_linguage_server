// src/main/java/tech/trvihnls/features/excercise/dtos/response/admin/MatchingExerciseDetailResponse.java
package tech.trvihnls.features.excercise.dtos.response.admin;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingExerciseDetailResponse {
    private long exerciseId;
    private String exerciseName;
    @Builder.Default
    private List<MatchingPairDetailResponse> matchingPairs = new ArrayList<>();
}
