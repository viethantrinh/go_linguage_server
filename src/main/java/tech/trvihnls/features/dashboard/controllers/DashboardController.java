package tech.trvihnls.features.dashboard.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.dashboard.dtos.response.DashboardResponse;
import tech.trvihnls.features.dashboard.services.DashboardService;

@RestController
@RequestMapping("/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardData() {
        DashboardResponse response = dashboardService.getDashboardData();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }
}
