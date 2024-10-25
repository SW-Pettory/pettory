package com.pettory.pettory.jointshopping.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.beans.ConstructorProperties;

@Getter
@Schema(description = "강퇴 요청")
public class WithdrawalRequest {

    @NotEmpty
    private final String userEmail;

    // JSON 파싱 과정에서 필드가 하나일 경우 이런식으로 생성자 선언 필요
    @ConstructorProperties(value = {"userEmail"})
    public WithdrawalRequest(String userEmail){
        this.userEmail = userEmail;
    }
}
