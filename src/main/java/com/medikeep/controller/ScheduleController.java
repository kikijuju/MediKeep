package com.medikeep.controller;

import com.medikeep.domain.User;
import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.ScheduleRequestDto;
import com.medikeep.dto.ScheduleResponseDto;
import com.medikeep.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Schedule API", description = "복용 일정 관리 API (JWT 인증 필요)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "스케줄 생성", description = "새로운 복용 스케줄을 등록. 하나의 약에 대해 여러 요일/시간대를 한번에 등록 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스케줄 등록 성공")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<Void>> createSchedules(
            @RequestBody ScheduleRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        scheduleService.createSchedules(requestDto, user);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "스케줄이 성공적으로 등록되었습니다.", null));
    }

    @Operation(summary = "스케줄 전체 조회", description = "사용자가 등록한 모든 복용 스케줄 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ScheduleResponseDto>>> getSchedules(
            @AuthenticationPrincipal User user
    ) {
        List<ScheduleResponseDto> schedules = scheduleService.getSchedules(user);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "스케줄 목록 조회 성공", schedules));
    }

    @Operation(summary = "스케줄 삭제", description = "특정 복용 스케줄을 ID로 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 스케줄 또는 권한 없음")
    })
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteSchedule(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal User user
    ) {
        try {
            scheduleService.deleteSchedule(scheduleId, user);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "스케줄이 삭제되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }
}
