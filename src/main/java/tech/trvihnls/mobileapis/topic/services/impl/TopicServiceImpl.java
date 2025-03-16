package tech.trvihnls.mobileapis.topic.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.*;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.*;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCode;
import tech.trvihnls.mobileapis.excercise.dtos.response.ExerciseDetailResponse;
import tech.trvihnls.mobileapis.excercise.dtos.response.VocabularyExerciseResponse;
import tech.trvihnls.mobileapis.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.mobileapis.topic.services.TopicService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final UserLessonAttemptRepository userLessonAttemptRepository;
    private final ExerciseRepository exerciseRepository;
    private final VocabularyExerciseRepository vocabularyExerciseRepository;
    private final WordRepository wordRepository;

    @Override
    public List<LessonDetailResponse> getTopicLessonDetailResponseData(long topicId) {
        // Find all related lessons belong to these topic (find list of lessons which have this topicId)
        List<Lesson> lessons = lessonRepository.findByTopicIdOrderByDisplayOrderAsc(topicId);

        // Calculate the user's xp points for lesson scope of this user (map the lesson's id with the totalUserXpPoints of that lesson)
        Long userId = SecurityUtils.getCurrentUserId();
        List<UserLessonAttempt> userAttemptedLessons = userLessonAttemptRepository.findByUserId(userId); // find all the lessons which attempted by specific user
        Map<Long, Integer> lessonXpPointsMap = new HashMap<>();
        for (var u : userAttemptedLessons) {
            if (lessons.contains(u.getLesson())) {
                lessonXpPointsMap.put(u.getLesson().getId(), u.getXpPointsEarned());
            }
        }

        // Build list of lessonDetailResponse
        List<LessonDetailResponse> lessonDetails = new ArrayList<>();
        for (var l : lessons) {
            // Build list of exerciseDetailResponse
            List<ExerciseDetailResponse<?>> exerciseDetails = new ArrayList<>();

            // Find all the exercise belong to this lesson by display order
            List<Exercise> exercises = exerciseRepository.findByLessonIdOrderByDisplayOrderAsc(l.getId());

            // Loop through each exercise and build xExerciseResponse (eg: VocabularyExerciseResponse, MultipleChoiceExerciseResponse,...)
            for (var e : exercises) {
                if (e.getVocabularyExercise() != null) {
                    exerciseDetails.add(buildVocabularyExerciseResponse(e));
                }

//                if (e.get....Exercise() != null) {  // for another exercise type
//
//                }
            }

            LessonDetailResponse lessonDetailResponse = LessonDetailResponse.builder()
                    .id(l.getId())
                    .name(l.getName())
                    .totalUserXPPoints(lessonXpPointsMap.getOrDefault(l.getId(), 0))
                    .lessonType(l.getLessonType().getId().intValue())
                    .displayOrder(l.getDisplayOrder())
                    .exercises(exerciseDetails)
                    .build();

            lessonDetails.add(lessonDetailResponse);
        }

        return lessonDetails;
    }

    private ExerciseDetailResponse<VocabularyExerciseResponse> buildVocabularyExerciseResponse(Exercise exercise) {

        // Find the word related to this vocabulary exercise
        Word word = exercise.getVocabularyExercise().getWord();

        // Find the related sentences and pick random one
        List<Sentence> relatedSentence = word.getSentences();

        if (relatedSentence.isEmpty()) {
            throw new ResourceNotFoundException(ErrorCode.RELATED_SENTENCES_NOT_EXISTED);
        }

        VocabularyExerciseResponse vocabularyExerciseResponse = VocabularyExerciseResponse.builder()
                .englishText(word.getEnglishText())
                .vietnameseText(word.getVietnameseText())
                .imageUrl(word.getImageUrl())
                .audioUrl(word.getAudioUrl())
                .sentence(
                        VocabularyExerciseResponse.SentenceResponse.builder()
                                .englishText(relatedSentence.get(0).getEnglishText())
                                .vietnameseText(relatedSentence.get(0).getVietnameseText())
                                .audioUrl(relatedSentence.get(0).getAudioUrl())
                                .build()
                )
                .build();

        return ExerciseDetailResponse.<VocabularyExerciseResponse>builder()
                .id(exercise.getId())
                .instruction(exercise.getInstruction())
                .displayOrder(exercise.getDisplayOrder())
                .data(vocabularyExerciseResponse)
                .build();
    }
}
