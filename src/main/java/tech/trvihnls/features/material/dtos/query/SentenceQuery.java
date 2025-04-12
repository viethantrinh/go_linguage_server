package tech.trvihnls.features.material.dtos.query;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentenceQuery {
    @Column(name = "id")
    private Long id;

    @Column(name = "english_text", nullable = false)
    private String englishText;

    @Column(name = "vietnamese_text", nullable = false)
    private String vietnameseText;

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;
}
