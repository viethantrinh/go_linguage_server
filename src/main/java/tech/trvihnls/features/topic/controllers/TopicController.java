package tech.trvihnls.features.topic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.features.topic.dtos.response.TopicAdminResponse;
import tech.trvihnls.features.topic.services.TopicService;

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

    @GetMapping("/admin/list")
    public ResponseEntity<ApiResponse<List<TopicAdminResponse>>> getAllTopicsForAdmin() {
        List<TopicAdminResponse> topics = topicService.getAllTopics();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, topics);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTopicForAdmin(@PathVariable("id") Long id) {
        topicService.deleteById(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
