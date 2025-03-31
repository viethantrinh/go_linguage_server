package tech.trvihnls.features.ai.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroqWhisperResponse {
    private String text;
}
