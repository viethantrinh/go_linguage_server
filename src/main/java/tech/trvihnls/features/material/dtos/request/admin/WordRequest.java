// src/main/java/tech/trvihnls/features/material/dtos/request/WordRequest.java
package tech.trvihnls.features.material.dtos.request.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordRequest {
    private String englishText;
    private String vietnameseText;
    private List<Long> topicIds;
    private List<Long> sentenceIds;
}
