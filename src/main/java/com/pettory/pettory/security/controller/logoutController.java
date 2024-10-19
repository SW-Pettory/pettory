package com.pettory.pettory.security.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logout")
public class logoutController {

    // 로그아웃
    @PostMapping
    public ResponseEntity<CommonResponseDTO> logoutFromService() {

        Long logoutUserId = ;

        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "로그아웃 성공", logoutUserId);
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }
}
