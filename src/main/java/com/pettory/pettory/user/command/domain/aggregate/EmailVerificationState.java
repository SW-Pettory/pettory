package com.pettory.pettory.user.command.domain.aggregate;

public enum EmailVerificationState {
    VALID,  // 유효함 - 아직 사용 가능
    INVALID, // 유효하지 않음 - 시간 만료
    ACCEPT  // 인증 완료되어 사용 불가
}
