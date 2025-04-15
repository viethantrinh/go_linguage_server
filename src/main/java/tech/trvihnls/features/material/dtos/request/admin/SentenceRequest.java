// src/main/java/tech/trvihnls/features/sentence/dtos/request/SentenceRequest.java
package tech.trvihnls.features.material.dtos.request.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentenceRequest {
    private String englishText;
    private String vietnameseText;
    private List<Long> topicIds;
    private List<Long> wordIds;
}
