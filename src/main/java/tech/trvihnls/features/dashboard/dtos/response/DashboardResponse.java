package tech.trvihnls.features.dashboard.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private int userCount;
    private int levelCount;
    private int topicCount;
    private int lessonCount;
    private int wordCount;
    private int sentenceCount;
    private double moneyCount;
}
