package com.pettory.pettory.user.command.domain.repository;

import com.pettory.pettory.user.command.domain.aggregate.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository {
    EmailVerification save(EmailVerification emailVerification);

    Optional<EmailVerification> findByUser_UserId(Long userId);

    void delete(EmailVerification emailVerification);
}
