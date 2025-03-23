package tech.trvihnls.mobileapis.lesson.services;

import tech.trvihnls.mobileapis.lesson.dtos.request.LessonSubmitRequest;

public interface LessonService {
    Object submitLesson(long lessonId, LessonSubmitRequest request);
}
