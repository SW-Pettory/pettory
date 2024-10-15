package com.pettory.pettory.user.command.domain.aggregate;

public enum UserState {
    ACTIVE,  // 활성화(인증)
    UNVERIFIED, // 미인증
    SUSPEND,  // 정지
    DELETE;   // 탈퇴
}
