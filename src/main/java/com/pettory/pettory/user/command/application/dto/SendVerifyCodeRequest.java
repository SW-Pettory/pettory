package com.pettory.pettory.user.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 이메일로 인증 코드 전송
@Getter
@RequiredArgsConstructor
public class SendVerifyCodeRequest {
    @NotBlank
    private String userEmail;
}
