// src/main/java/tech/trvihnls/features/excercise/dtos/request/admin/MatchingExerciseUpdateRequest.java
package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingExerciseUpdateRequest {
    private long exerciseId;
    private List<MatchingPairUpdateRequest> matchingPairs;
}
