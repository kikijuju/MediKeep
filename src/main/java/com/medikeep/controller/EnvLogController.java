package com.medikeep.controller;

import com.medikeep.domain.EnvLog;
import com.medikeep.domain.User;
import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.EnvLogResponseDto;
import com.medikeep.dto.EnvUploadRequestDto;
import com.medikeep.service.EnvLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Environment API", description = "온도/습도 업로드 및 조회 API (JWT 인증 필요)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/env")
public class EnvLogController {

    private final EnvLogService envLogService;

    @Operation(summary = "온습도 업로드", description = "앱에서 10분마다 수집된 온도/습도를 업로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공")
    })
    @PostMapping("/upload")
    public ResponseEntity<ApiResponseDto<EnvLogResponseDto>> upload(
            @RequestBody EnvUploadRequestDto req,
            @AuthenticationPrincipal User user
    ) {
        EnvLog saved = envLogService.upload(user, req);
        return ResponseEntity.ok(new ApiResponseDto<>(
                HttpStatus.OK, "업로드 성공", EnvLogResponseDto.from(saved)
        ));
    }


}