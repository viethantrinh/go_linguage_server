package tech.trvihnls.features.lesson.services;

import tech.trvihnls.features.lesson.dtos.request.LessonSubmitRequest;
import tech.trvihnls.features.lesson.dtos.response.LessonSubmitResponse;

public interface LessonService {
    LessonSubmitResponse submitLesson(long lessonId, LessonSubmitRequest request);
}
