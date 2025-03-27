package tech.trvihnls.mobileapis.main.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationResponse;
import tech.trvihnls.mobileapis.main.dtos.response.HomeResponse;
import tech.trvihnls.mobileapis.main.dtos.response.ReviewResponse;
import tech.trvihnls.mobileapis.main.services.MainService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<HomeResponse>> getHomeData() {
        HomeResponse response = mainService.retrieveHomeData();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/review")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewData() {
        ReviewResponse response = mainService.retrieveReviewData();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @GetMapping("/conversation")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getConversationData() {
        List<ConversationResponse> response = mainService.retrieveConversationData();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }
}
