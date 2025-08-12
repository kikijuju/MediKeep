package com.medikeep.controller;

import com.medikeep.domain.User;
import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.LogCheckRequestDto;
import com.medikeep.dto.LogResponseDto;
import com.medikeep.dto.MonthlyLogResponseDto;
import com.medikeep.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Log API", description = "복용 기록 관리 API (JWT 인증 필요)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    @Operation(summary = "복용 완료 체크", description = "사용자가 특정 시간대의 약을 복용했다고 기록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기록 성공"),
            @ApiResponse(responseCode = "400", description = "해당 시간대에 복용할 약 없음")
    })
    @PostMapping("/check")
    public ResponseEntity<ApiResponseDto<Void>> checkIntake(
            @RequestBody LogCheckRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        try {
            logService.checkIntake(requestDto, user);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "복용 기록이 저장되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }

    @Operation(summary = "일별 복용 기록 조회", description = "특정 날짜의 모든 복용 기록 조회")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<LogResponseDto>>> getDailyLogs(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal User user
    ) {
        List<LogResponseDto> logs = logService.getDailyLogs(date, user);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "일별 복용 기록 조회 성공", logs));
    }

    @Operation(summary = "월별 복용 기록 조회 (달력용)", description = "특정 월에 복용한 날짜 목록을 조회하여 달력 UI에 표시")
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponseDto<MonthlyLogResponseDto>> getMonthlyLogs(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @AuthenticationPrincipal User user
    ) {
        MonthlyLogResponseDto monthlyLogs = logService.getMonthlyLogs(year, month, user);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "월별 복용 기록 조회 성공", monthlyLogs));
    }
}
