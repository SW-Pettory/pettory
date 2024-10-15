package com.pettory.pettory.user.command.infrastructure.repository;

import com.pettory.pettory.user.command.domain.aggregate.EmailVerification;
import com.pettory.pettory.user.command.domain.repository.EmailVerificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailVerificationRepository extends JpaRepository<EmailVerification, Long>, EmailVerificationRepository {
}
