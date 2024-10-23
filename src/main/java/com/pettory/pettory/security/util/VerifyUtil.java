package com.pettory.pettory.security.util;


import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;

@Slf4j
public class VerifyUtil {

    // 회원의 임시 비밀번호를 생성하는 메소드
    public static String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);    // 8자리 임시 비밀번호
        // TODO: 여기도 비밀번호 규칙 지켜야 하나?
    }


    // 이메일 인증을 위한 6자리 인증 코드를 생성하는 메소드(영문, 숫자 조합)
    public static String generateVerifyCode() {
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();

        for (int i = 0; i < 6; i++) {
            if (rd.nextBoolean()) { // true or false 생성
                sb.append(rd.nextInt(10));  // true 이면 숫자
            } else {
                sb.append((char)(rd.nextInt(26) + 65)); // false 이면 영문
            }
        }
        log.info(sb.toString());
        return sb.toString();
    }

}
