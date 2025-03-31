package tech.trvihnls.features.lesson.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonSubmitRequest {
    private int xpPoints;
    private int goPoints;
}
