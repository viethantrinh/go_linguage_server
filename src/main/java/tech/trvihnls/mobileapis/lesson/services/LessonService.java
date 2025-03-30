package tech.trvihnls.mobileapis.lesson.services;

import tech.trvihnls.mobileapis.lesson.dtos.request.LessonSubmitRequest;
import tech.trvihnls.mobileapis.lesson.dtos.response.LessonSubmitResponse;

public interface LessonService {
    LessonSubmitResponse submitLesson(long lessonId, LessonSubmitRequest request);
}
