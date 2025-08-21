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

    @Operation(summary = "장치 상태 업데이트", description = "ESP32 장치로부터 현재 상태를 수신하여 업데이트하고, 약 복용을 감지합니다.")
    @PostMapping("/status")
    public ResponseEntity<ApiResponseDto<Void>> updateDeviceStatus(@RequestBody DeviceStatusUpdateRequestDto requestDto) {
        try {
            deviceService.updateDeviceStatusAndLogIntake(requestDto);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "장치 상태가 성공적으로 업데이트되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }
}
