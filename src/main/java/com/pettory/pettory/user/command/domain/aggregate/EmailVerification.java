package com.pettory.pettory.user.command.domain.aggregate;


import com.pettory.pettory.exception.NotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE email_verification SET email_verification_state = 'ACCEPT', email_verification_delete_datetime = NOW() WHERE email_verification_id = ? AND email_verification_state != 'INVALID'")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailVerificationId;

    private String emailVerificationCode;

    private LocalDateTime emailVerificationCodeExpirationDatetime;

    private Long emailVerificationSendCount = 1L;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime emailVerificationUpdateDatetime;

    @CreatedDate
    private LocalDateTime emailVerificationCreateDatetime;

    private LocalDateTime emailVerificationDeleteDatetime;

    @Enumerated(EnumType.STRING)
    private EmailVerificationState emailVerificationState = EmailVerificationState.VALID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public EmailVerification(
            String emailVerificationCode,
            LocalDateTime emailVerificationCodeExpirationDatetime

    ) {
        this.emailVerificationCode = emailVerificationCode;
        this.emailVerificationCodeExpirationDatetime = emailVerificationCodeExpirationDatetime;
    }


    // 이메일 인증 코드를 생성하는 메소드
    public static EmailVerification createEmailVerification(
            String emailVerificationCode,
            LocalDateTime emailVerificationCodeExpirationDatetime
    ) {
        return new EmailVerification(emailVerificationCode, emailVerificationCodeExpirationDatetime);
    }


    // 해당 인증 코드가 유효한지 확인하는 메소드
    public boolean isValid() {
        // INVALID, ACCEPT 는 유효하지 않은 인증 코드를 의미함
        // 인증 코드의 만료 시간이 현재 시간 이후인지 확인
        return this.emailVerificationState.equals(EmailVerificationState.VALID)
                && this.emailVerificationCodeExpirationDatetime.isAfter(LocalDateTime.now());
    }

    // 이메일 인증 코드 전송 횟수를 증가시키는 메소드
    public void updateSendCount(Long expirationTime) {
        this.emailVerificationSendCount++;
    }

    // 이메일 인증 객체에 회원을 추가하는 메소드
    public void addUserToEmailVerification(User user) {
        if (user == null) {
            throw new NotFoundException("회원을 찾을 수 없습니다.");
        }
        this.user = user;
    }
}
