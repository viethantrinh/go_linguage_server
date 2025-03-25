package tech.trvihnls.mobileapis.topic.services;

import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.mobileapis.excercise.dtos.response.ExerciseDetailResponse;
import tech.trvihnls.mobileapis.excercise.dtos.response.MultipleChoiceExerciseResponse;
import tech.trvihnls.mobileapis.excercise.dtos.response.WordArrangementExerciseResponse;
import tech.trvihnls.mobileapis.lesson.dtos.response.LessonDetailResponse;

import java.util.List;

public interface TopicService {
    List<LessonDetailResponse> getTopicLessonDetailResponseData(long topicId);

    default ExerciseDetailResponse<WordArrangementExerciseResponse> buildWordArrangementExerciseResponse(Exercise exercise) {
        return null;
    }

    default ExerciseDetailResponse<MultipleChoiceExerciseResponse> buildMultipleChoiceExerciseResponse(Exercise exercise) {
        return null;
    }
}
