// src/main/java/tech/trvihnls/features/sentence/dtos/response/SentenceResponse.java
package tech.trvihnls.features.material.dtos.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentenceResponse {
    private Long id;
    private String englishText;
    private String vietnameseText;
    private String audioUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> topicIds;
    private List<Long> wordIds;
}
