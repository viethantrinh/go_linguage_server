package tech.trvihnls.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.models.dtos.base.ApiResponse;
import tech.trvihnls.models.dtos.main.HomeResponse;
import tech.trvihnls.services.MainService;
import tech.trvihnls.utils.ResponseUtils;
import tech.trvihnls.utils.enums.SuccessCode;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<HomeResponse>> getHomeData() {
        HomeResponse response = mainService.retrieveHomeData();
        return ResponseUtils.success(SuccessCode.GENERAL_SUCCESS, response);
    }
}
