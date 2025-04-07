package tech.trvihnls.features.topic.services;

import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.features.excercise.dtos.response.ExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.MultipleChoiceExerciseResponse;
import tech.trvihnls.features.excercise.dtos.response.WordArrangementExerciseResponse;
import tech.trvihnls.features.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.features.topic.dtos.response.TopicAdminResponse;

import java.util.List;

public interface TopicService {
    List<LessonDetailResponse> getTopicLessonDetailResponseData(long topicId);

    List<TopicAdminResponse> getAllTopics();
    void deleteById(Long id);

    default ExerciseDetailResponse<WordArrangementExerciseResponse> buildWordArrangementExerciseResponse(Exercise exercise) {
        return null;
    }

    default ExerciseDetailResponse<MultipleChoiceExerciseResponse> buildMultipleChoiceExerciseResponse(Exercise exercise) {
        return null;
    }
}
