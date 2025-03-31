package tech.trvihnls.commons.domains;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WordTimestamp {
    private String text;
    @Builder.Default
    private List<WordTime> words = new ArrayList<>();
}
