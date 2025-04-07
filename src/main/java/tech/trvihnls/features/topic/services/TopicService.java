package tech.trvihnls.features.topic.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.features.excercise.dtos.response.ExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.MultipleChoiceExerciseResponse;
import tech.trvihnls.features.excercise.dtos.response.WordArrangementExerciseResponse;
import tech.trvihnls.features.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.features.topic.dtos.request.TopicCreateAdminRequest;
import tech.trvihnls.features.topic.dtos.request.TopicUpdateAdminRequest;
import tech.trvihnls.features.topic.dtos.response.*;

import java.util.List;

public interface TopicService {
    List<LessonDetailResponse> getTopicLessonDetailResponseData(long topicId);

    List<TopicAdminResponse> getAllTopics();
    void deleteById(Long id);

    TopicCreateAdminResponse createTopic(TopicCreateAdminRequest request);
    TopicImageResponse createOrUpdateImage(MultipartFile file, Long topicId);
    TopicDetailAdminResponse getTopicDetail(Long topicId);
    TopicUpdateAdminResponse updateTopic(TopicUpdateAdminRequest request);

    default ExerciseDetailResponse<WordArrangementExerciseResponse> buildWordArrangementExerciseResponse(Exercise exercise) {
        return null;
    }

    default ExerciseDetailResponse<MultipleChoiceExerciseResponse> buildMultipleChoiceExerciseResponse(Exercise exercise) {
        return null;
    }
}
