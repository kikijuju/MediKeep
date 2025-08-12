package com.medikeep.controller;

import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.DeviceStatusUpdateRequestDto;
import com.medikeep.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Device API", description = "ESP32 장치 상태 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "장치 상태 업데이트", description = "ESP32 장치로부터 현재 상태(온도, 습도, 약 유무)를 주기적으로 수신하여 업데이트. 인증 필요 X")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류 (예: 존재하지 않는 userId)")
    })
    @PostMapping("/status")
    public ResponseEntity<ApiResponseDto<Void>> updateDeviceStatus(@RequestBody DeviceStatusUpdateRequestDto requestDto) {
        try {
            deviceService.updateDeviceStatus(requestDto);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "장치 상태가 성공적으로 업데이트되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }
}
