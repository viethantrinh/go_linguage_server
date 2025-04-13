// src/main/java/tech/trvihnls/features/excercise/dtos/request/admin/MatchingPairCreateRequest.java
package tech.trvihnls.features.excercise.dtos.request.admin;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPairCreateRequest {
    private Long wordId;
}
