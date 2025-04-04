package tech.trvihnls.features.dashboard.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.repositories.*;
import tech.trvihnls.features.dashboard.dtos.response.DashboardResponse;
import tech.trvihnls.features.dashboard.services.DashboardService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final WordRepository wordRepository;
    private final SentenceRepository sentenceRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public DashboardResponse getDashboardData() {
        long userCount = userRepository.count();
        long levelCount = levelRepository.count();
        long topicCount = topicRepository.count();
        long lessonCount = lessonRepository.count();
        long wordCount = wordRepository.count();
        long sentenceCount = sentenceRepository.count();
        BigDecimal moneyCount = userSubscriptionRepository.calculateTotalRevenue();


        return DashboardResponse.builder()
                .userCount((int) userCount)
                .levelCount((int) levelCount)
                .topicCount((int) topicCount)
                .lessonCount((int) lessonCount)
                .wordCount((int) wordCount)
                .sentenceCount((int) sentenceCount)
                .moneyCount(moneyCount != null ? moneyCount.doubleValue() : 0d)
                .build();
    }
}
