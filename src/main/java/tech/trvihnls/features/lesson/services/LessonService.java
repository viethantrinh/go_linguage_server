package tech.trvihnls.features.lesson.services;

import tech.trvihnls.features.lesson.dtos.request.LessonCreateAdminRequest;
import tech.trvihnls.features.lesson.dtos.request.LessonSubmitRequest;
import tech.trvihnls.features.lesson.dtos.request.LessonUpdateAdminRequest;
import tech.trvihnls.features.lesson.dtos.response.*;

import java.util.List;

public interface LessonService {
    LessonSubmitResponse submitLesson(long lessonId, LessonSubmitRequest request);
    List<LessonAdminResponse> getAllLessons();
    void deleteLessonById(Long id);
    LessonCreateAdminResponse createLesson(LessonCreateAdminRequest request);
    LessonDetailAdminResponse getLessonDetail(Long id);
    LessonUpdateAdminResponse updateLesson(LessonUpdateAdminRequest request);
}
