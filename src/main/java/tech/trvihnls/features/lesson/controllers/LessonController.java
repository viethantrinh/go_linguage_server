package tech.trvihnls.features.lesson.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.lesson.dtos.request.LessonCreateAdminRequest;
import tech.trvihnls.features.lesson.dtos.request.LessonSubmitRequest;
import tech.trvihnls.features.lesson.dtos.request.LessonUpdateAdminRequest;
import tech.trvihnls.features.lesson.dtos.response.*;
import tech.trvihnls.features.lesson.services.LessonService;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Validated
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<LessonSubmitResponse>> submitLessonResult(@PathVariable("id") String id,
                                                                                @RequestBody @Valid LessonSubmitRequest request) {
        LessonSubmitResponse response = lessonService.submitLesson(Long.parseLong(id), request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/admin/list")
    public ResponseEntity<ApiResponse<List<LessonAdminResponse>>> getAllLessonsByAdmin() {
        List<LessonAdminResponse> lessons = lessonService.getAllLessons();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, lessons);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLessonByAdmin(@PathVariable("id") Long id) {
        lessonService.deleteLessonById(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<ApiResponse<LessonCreateAdminResponse>> createLesson(
            @RequestBody LessonCreateAdminRequest request) {
        LessonCreateAdminResponse response = lessonService.createLesson(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/admin/{id}/detail")
    public ResponseEntity<ApiResponse<LessonDetailAdminResponse>> getLessonDetailByAdmin(
            @PathVariable("id") Long id) {
        LessonDetailAdminResponse response = lessonService.getLessonDetail(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PutMapping("/admin/update")
    public ResponseEntity<ApiResponse<LessonUpdateAdminResponse>> updateLessonByAdmin(
            @RequestBody LessonUpdateAdminRequest request) {
        LessonUpdateAdminResponse response = lessonService.updateLesson(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }
}
