package tech.trvihnls.features.topic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.features.topic.dtos.request.TopicCreateAdminRequest;
import tech.trvihnls.features.topic.dtos.request.TopicUpdateAdminRequest;
import tech.trvihnls.features.topic.dtos.response.*;
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

    @PostMapping("/admin/create")
    public ResponseEntity<ApiResponse<TopicCreateAdminResponse>> createTopicByAdmin(
            @RequestBody TopicCreateAdminRequest request
    ) {
        var response = topicService.createTopic(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/admin/upload-image")
    public ResponseEntity<ApiResponse<TopicImageResponse>> uploadImageForTopicByAdmin(
            @RequestParam("file") MultipartFile file,
            @RequestParam("topicId") Long topicId
    ) {
        var response = topicService.createOrUpdateImage(file, topicId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/admin/{id}/detail")
    public ResponseEntity<ApiResponse<TopicDetailAdminResponse>> getTopicDetailByAdmin(
            @PathVariable Long id) {
        var topics = topicService.getTopicDetail(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, topics);
    }

    @PutMapping("/admin/update")
    public ResponseEntity<ApiResponse<TopicUpdateAdminResponse>> updateTopicDetailByAdmin(
            @RequestBody TopicUpdateAdminRequest request
    ) {
        var topics = topicService.updateTopic(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, topics);
    }
}
