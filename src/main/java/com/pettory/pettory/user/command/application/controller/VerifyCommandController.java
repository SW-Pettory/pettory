package com.pettory.pettory.user.command.application.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import com.pettory.pettory.user.command.application.dto.FindEmailRequest;
import com.pettory.pettory.user.command.application.dto.SendVerifyCodeRequest;
import com.pettory.pettory.user.command.application.dto.VerifyCodeRequest;
import com.pettory.pettory.user.command.application.service.VerifyCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class VerifyCommandController {

    private final VerifyCommandService verifyCommandService;

    // 회원가입 후 이메일 인증 코드 검증
    @PostMapping("/emails/codes")
    public ResponseEntity<CommonResponseDTO> sendEmails(
            @RequestBody SendVerifyCodeRequest sendVerifyCodeRequest,
            @RequestParam String verifyCode
    ) {

        boolean isValid = verifyCommandService.isCodeValid(sendVerifyCodeRequest, verifyCode);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "인증 코드 검증 성공", isValid);

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }


//    // 이메일 찾기 - 인증 코드 전송
//    @PostMapping("/emails/nicknames")
//    public ResponseEntity<CommonResponseDTO> findEmails(@RequestBody FindEmailRequest findEmailRequest) {
//
//        verifyCommandService.sendVerifyCodeByUserNickname(findEmailRequest.getUserNickname());
//
//        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "이메일 발송 성공", null);
//        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
//    }

    // 이메일 찾기 - 인증 코드 검증(2)
//    @PostMapping("/emails/codes")
//    public ResponseEntity<CommonResponseDTO> verifyCode(@RequestBody VerifyCodeRequest verifyCodeRequest) {
//
//        verifyCommandService.checkCode(verifyCodeRequest.getUserEmail(), verifyCodeRequest.getVerifyCode());
//
//        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "인증 코드 검증 성공", null);
//        return ResponseEntity.ok(successResponse);
//    }

}
