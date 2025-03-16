package tech.trvihnls.mobileapis.topic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.mobileapis.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.mobileapis.topic.services.TopicService;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/{id}/lessons/detail")
    public ResponseEntity<ApiResponse<List<LessonDetailResponse>>> getTopicDetailLessonLearningMaterial(@PathVariable("id") long id) {
        List<LessonDetailResponse> response = topicService.getTopicLessonDetailResponseData(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }
}
