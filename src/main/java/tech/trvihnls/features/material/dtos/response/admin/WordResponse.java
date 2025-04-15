// src/main/java/tech/trvihnls/features/material/dtos/response/WordResponse.java
package tech.trvihnls.features.material.dtos.response.admin;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordResponse {
    private Long id;
    private String englishText;
    private String vietnameseText;
    private String imageUrl;
    private String audioUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> topicIds;
    private List<Long> sentenceIds;
}
