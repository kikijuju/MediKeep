package com.medikeep.controller;

import com.medikeep.dto.ApiResponseDto;
import com.medikeep.dto.UserLoginRequestDto;
import com.medikeep.dto.UserSignupRequestDto;
import com.medikeep.service.UserService;
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

@Tag(name = "User API", description = "사용자 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService; // <-- final 키워드 추가

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 이메일")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<Void>> signup(@RequestBody UserSignupRequestDto requestDto) {
        try {
            userService.signup(requestDto);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "회원가입이 성공적으로 완료되었습니다.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }

    @Operation(summary = "로그인", description = "로그인 후 JWT를 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환"),
            @ApiResponse(responseCode = "400", description = "이메일 또는 비밀번호 불일치")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<String>> login(@RequestBody UserLoginRequestDto requestDto) {
        try {
            String token = userService.login(requestDto);
            return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "로그인에 성공하였습니다.", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }
}
