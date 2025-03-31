package tech.trvihnls.commons.domains;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordTime {
    private String word;
    private double start;
    private double end;
}
