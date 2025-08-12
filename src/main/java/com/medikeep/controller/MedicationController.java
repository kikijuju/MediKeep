package com.medikeep.controller;

import com.medikeep.domain.User;
import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.MedicationRequestDto;
import com.medikeep.dto.MedicationResponseDto;
import com.medikeep.service.MedicationService;
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

@Tag(name = "Medication API", description = "약/영양제 정보 관리 API (JWT 인증 필요)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    @Operation(summary = "약/영양제 등록", description = "새로운 약 또는 영양제 정보 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "이미 등록된 약")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<Void>> createMedication(
            @RequestBody MedicationRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        try {
            medicationService.createMedication(requestDto, user);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "약이 성공적으로 등록되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }

    @Operation(summary = "등록된 약/영양제 목록 조회", description = "사용자가 등록한 모든 약/영양제 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<MedicationResponseDto>>> getMedications(
            @AuthenticationPrincipal User user
    ) {
        List<MedicationResponseDto> medications = medicationService.getMedications(user);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "약 목록 조회 성공", medications));
    }

    @Operation(summary = "등록된 약/영양제 삭제", description = "특정 약/영양제 정보 & 관련 스케줄 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 약 또는 권한 없음")
    })
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteMedication(
            @PathVariable Long medicationId,
            @AuthenticationPrincipal User user
    ) {
        try {
            medicationService.deleteMedication(medicationId, user);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "약이 삭제되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }
}
