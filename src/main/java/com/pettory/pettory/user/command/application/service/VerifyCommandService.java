package com.pettory.pettory.user.command.application.service;

import com.pettory.pettory.exception.InvalidCodeException;
import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.security.util.EmailService;
import com.pettory.pettory.security.util.VerifyUtil;
import com.pettory.pettory.user.command.application.dto.SendVerifyCodeRequest;
import com.pettory.pettory.user.command.domain.aggregate.EmailVerification;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.EmailVerificationRepository;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyCommandService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    // 회원가입 시 이메일 인증 - 인증 코드 전송
    @Transactional
    public void sendVerifyCodeByUserUserEmail(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("가입한 이메일을 찾을 수 없습니다."));

        // 8자리 인증 코드 생성
        String verifyCode = VerifyUtil.generateVerifyCode();

        // 인증 코드 저장
        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(120);
        // todo: 600 으로 바꾸기

        EmailVerification emailVerification = EmailVerification.createEmailVerification(verifyCode, expirationTime);

        emailVerification.addUserToEmailVerification(user);

        // 이메일 인증 엔터티 저장
        emailVerificationRepository.save(emailVerification);

        // 이메일로 인증 코드 전송
        emailService.sendEmail(
                user.getUserEmail(),
                "펫토리 인증 코드 안내",
                user.getUserNickname() + " 님의 이메일 인증 코드는 " +
                        verifyCode + " 입니다."
        );
    }

    // 이메일 인증 코드 검증
    @Transactional
    public boolean isCodeValid(SendVerifyCodeRequest sendVerifyCodeRequest, String verifyCode) {

        User user = userRepository.findByUserEmail(sendVerifyCodeRequest.getUserEmail())
                .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

        // 저장된 이메일 인증 코드 가져옴
        EmailVerification emailVerification = emailVerificationRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new NotFoundException("이메일 인증 코드가 존재하지 않습니다."));

        String savedCode = emailVerification.getEmailVerificationCode();

        // 인증 코드가 존재하는지 확인
        if (savedCode == null) {
            throw new NotFoundException("존재하지 않습니다.");
        }

        // 인증 코드의 만료 시간 확인
        if (emailVerification.getEmailVerificationCodeExpirationDatetime().isBefore(LocalDateTime.now())) {
            throw new InvalidCodeException("인증 코드가 만료되었습니다.");
        }

        // 회원이 입력한 인증 코드와 저장된 인증 코드가 같은지 확인
        if (savedCode.equals(verifyCode)) {
            // 인증 코드가 같으면 해당 인증 코드를 삭제, 회원 상태를 ACTIVE 로 변경
            log.info("인증 코드 일치");
            user.updateUserAsActive();
            emailVerificationRepository.delete(emailVerification);
            return true;
        } else {
            log.info("인증 코드 불일치! 입력한 인증 코드: " + verifyCode + "등록된 인증 코드" + savedCode);
            throw new InvalidCodeException("잘못된 인증 코드 입니다.");
        }

    }


    // 닉네임으로 이메일 찾기 - 인증 코드 전송
//    @Transactional
//    public void sendVerifyCodeByUserNickname(String userNickname) {
//        User user = userRepository.findByUserNickname(userNickname)
//                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
//
//        // 인증 코드 생성
//        String verifyCode = VerifyUtil.generateVerifyCode();
//
//        // 인증 코드 저장
//        saveVerifyCode(user.getUserEmail(), verifyCode);
//
//        // 이메일로 인증 코드 전송
//        emailService.sendEmail(
//                user.getUserEmail(),
//                "펫토리 인증 코드 안내",
//                user.getUserNickname() + " 님의 이메일 찾기 인증 코드는 " +
//                        verifyCode +" 입니다."
//        );
//    }

    // 인증 코드 저장
//    public void saveVerifyCode(String userEmail, String verifyCode) {
//        verifyCodes.put(userEmail, verifyCode);
//    }
//
//    // 인증 코드 검증
//    public boolean isCodeValid(String userEmail, String verifyCode) {
//        return verifyCode.equals(verifyCodes.get(userEmail));
//    }
//
//    // 검증에 성공하면 인증 코드 삭제
//    public void removeVerifyCode(String userEmail) {
//        verifyCodes.remove(userEmail);
//    }

//    // 닉네임으로 이메일 찾기 - 인증 코드 검증
//    @Transactional
//    public void checkCode(String userEmail, String verifyCode) {
//        if (!isCodeValid(userEmail, verifyCode)) {
//            throw new UnauthorizedException("인증 코드가 일치하지 않습니다.");
//        }
//        // 검증 성공하면 인증 코드 삭제
//        removeVerifyCode(userEmail);
//    }
}
