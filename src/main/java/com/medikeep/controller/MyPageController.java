package com.medikeep.controller;

import com.medikeep.domain.User;
import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.CalendarEventDto;
import com.medikeep.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "MyPage API", description = "마이페이지 관련 API (JWT 인증 필요)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "월별 달력 데이터 조회", description = "특정 월의 모든 스케줄 및 복용 기록을 달력에 표시하기 위한 데이터로 조회")
    @GetMapping("/calendar")
    public ResponseEntity<ApiResponseDto<List<CalendarEventDto>>> getCalendarEvents(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @AuthenticationPrincipal User user
    ) {
        List<CalendarEventDto> events = myPageService.getCalendarEvents(year, month, user);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "월별 달력 데이터 조회 성공", events));
    }
}
