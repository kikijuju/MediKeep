package com.medikeep.controller;

import com.medikeep.domain.User;
import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.DashboardResponseDto;
import com.medikeep.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home API", description = "대시보드(홈) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "대시보드 정보 조회", description = "홈 화면에 필요한 모든 정보 조회 (JWT 인증 필요)")
    @GetMapping
    public ResponseEntity<ApiResponseDto<DashboardResponseDto>> getDashboardInfo(
            @AuthenticationPrincipal User user
    ) {
        DashboardResponseDto dashboardInfo = homeService.getDashboardInfo(user);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "대시보드 정보 조회 성공", dashboardInfo));
    }
}
