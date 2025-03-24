package tech.trvihnls.mobileapis.ai.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroqWhisperResponse {
    private String text;
}
