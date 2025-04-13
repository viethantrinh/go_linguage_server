// src/main/java/tech/trvihnls/features/excercise/dtos/request/admin/MatchingExerciseCreateRequest.java
package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingExerciseCreateRequest {
    private long exerciseId;
    private List<MatchingPairCreateRequest> matchingPairs;
}
