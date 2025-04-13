// src/main/java/tech/trvihnls/features/excercise/dtos/request/admin/MatchingPairUpdateRequest.java
package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPairUpdateRequest {
    private Long id;
    private Long wordId;
}
