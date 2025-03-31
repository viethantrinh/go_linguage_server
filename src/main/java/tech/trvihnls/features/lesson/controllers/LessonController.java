package tech.trvihnls.features.lesson.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.lesson.dtos.request.LessonSubmitRequest;
import tech.trvihnls.features.lesson.services.LessonService;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Validated
public class LessonController {

    private final LessonService lessonService;

    @PutMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<Object>> submitLessonResult(@PathVariable("id") String id,
                                                                  @RequestBody @Valid LessonSubmitRequest request) {
        Object o = lessonService.submitLesson(Long.parseLong(id), request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, o);
    }

}
