package tech.trvihnls.mobileapis.topic.services;

import tech.trvihnls.mobileapis.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.mobileapis.topic.dtos.response.TopicDetailResponse;

import java.util.List;

public interface TopicService {
    List<LessonDetailResponse> getTopicLessonDetailResponseData(long topicId);
}
