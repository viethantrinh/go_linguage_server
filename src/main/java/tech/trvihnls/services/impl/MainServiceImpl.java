package tech.trvihnls.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.trvihnls.exceptions.ResourceNotFoundException;
import tech.trvihnls.models.dtos.level.LevelResponse;
import tech.trvihnls.models.dtos.main.HomeResponse;
import tech.trvihnls.models.dtos.topic.TopicResponse;
import tech.trvihnls.models.entities.*;
import tech.trvihnls.repositories.*;
import tech.trvihnls.services.MainService;
import tech.trvihnls.utils.SecurityUtils;
import tech.trvihnls.utils.enums.ErrorCode;
import tech.trvihnls.utils.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserLessonAttemptRepository userLessonAttemptRepository;
    private final LevelRepository levelRepository;
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;


    @Override
    public HomeResponse retrieveHomeData() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED));

        boolean isUserSubscribed = userSubscriptionRepository.existsByIsActiveAndPaymentStatus(true, PaymentStatus.SUCCEEDED.getValue());

        // find all the lesson ids which belong to this user in user attempt table (mean what lessons that user have learned)
        List<UserLessonAttempt> lessonsByUser = userLessonAttemptRepository.findByUserId(user.getId());

        log.debug("Lesson IDs of user's ID {}: {}", user.getId(), lessonsByUser);

        // create a map which the keys are user's lesson attempted IDs, value is the current xp points of that lesson
        Map<Long, Integer> lessonXPMap = new HashMap<>();
        for (var l : lessonsByUser) {
            lessonXPMap.put(l.getId().getLessonId(), l.getXpPointsEarned());
        }

        // fetch all levels with their topics and lessons
        List<Level> levels = levelRepository.findAll(Sort.by("displayOrder").ascending());
        List<LevelResponse> levelResponses = new ArrayList<>();

        for (Level level : levels) {
            // Get topics for this level
            List<Topic> topics = topicRepository.findByLevelIdOrderByDisplayOrderAsc(level.getId());
            List<TopicResponse> topicResponses = new ArrayList<>();
            int levelTotalXp = 0;

            for (Topic topic : topics) {
                // Get lessons for this topic
                List<Lesson> lessons = lessonRepository.findByTopicIdOrderByDisplayOrderAsc(topic.getId());
                int topicTotalXp = 0;

                for (Lesson lesson : lessons) {
                    topicTotalXp += lessonXPMap.getOrDefault(lesson.getId(), 0);
                }

                levelTotalXp += topicTotalXp;

                TopicResponse topicResponse = TopicResponse.builder()
                        .id(topic.getId())
                        .name(topic.getName())
                        .imageUrl(topic.getImageUrl())
                        .displayOrder(topic.getDisplayOrder())
                        .isPremium(!isUserSubscribed) // if user is premium => unlock all topic by setting this to false
                        .totalUserXPPoints(topicTotalXp)
                        .build();

                topicResponses.add(topicResponse);
            }

            LevelResponse levelResponse = LevelResponse.builder()
                    .id(level.getId())
                    .name(level.getName())
                    .displayOrder(level.getDisplayOrder())
                    .totalUserXPPoints(levelTotalXp)
                    .topics(topicResponses)
                    .build();

            levelResponses.add(levelResponse);
        }

        return HomeResponse.builder()
                .streakPoints(user.getTotalStreakPoints())
                .goPoints(user.getTotalGoPoints())
                .isSubscribed(isUserSubscribed)
                .levels(levelResponses)
                .build();
    }
}
