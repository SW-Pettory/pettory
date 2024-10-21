package com.pettory.pettory.jointshopping.command.application.service;

import com.pettory.pettory.exception.BadJoinException;
import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingDeliveryInfoRequest;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingParticipationRequest;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingParticipationUser;
import com.pettory.pettory.jointshopping.command.domain.aggregate.ProvisionRecord;
import com.pettory.pettory.jointshopping.command.domain.service.JointShoppingGroupDomainService;
import com.pettory.pettory.jointshopping.command.domain.service.JointShoppingParticipationDomainService;
import com.pettory.pettory.jointshopping.command.domain.service.ProvisionRecordDomainService;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JointShoppingParticipationApplicationService {

    private final JointShoppingGroupDomainService jointShoppingGroupDomainService;
    private final JointShoppingParticipationDomainService jointShoppingParticipationDomainService;
    private final ProvisionRecordDomainService provisionRecordDomainService;
    private final UserRepository userRepository;

    /* 공동구매 참가 등록 */
    @Transactional
    public JointShoppingParticipationUser createParticipation(String userEmail, JointShoppingParticipationRequest participationRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        /* 참가가 가득 찼는지 체크 */
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupDomainService.findGroup(participationRequest.getJointShoppingGroupNum());
        // 최대 참가자 수를 불러옴
        int max = jointShoppingGroup.getJointShoppingParticipationMaximumCount();
        // 현재 참가자 수를 불러옴
        int now = jointShoppingParticipationDomainService.findUserCount(jointShoppingGroup);
        if (now >= max) {
            throw new BadJoinException("참가자가 많아 참가하실수 없습니다.");
        }

        /*결제 기능을 여기에 추가*/

        /* JointShoppingParticipationUser 도메인 로직 실행, entity 반환 */
        JointShoppingParticipationUser newJointShoppingParticipationUser
                = jointShoppingParticipationDomainService.createParticipation(participationRequest, jointShoppingGroup, user);

        /* save 로직 실행 */
        JointShoppingParticipationUser jointShoppingParticipationUser
                = jointShoppingParticipationDomainService.saveParticipation(newJointShoppingParticipationUser);

        /* 참가인원이 가득찰 시 */
        if (now + 1 == max) {
            /* 모임 상품 상태 변경(주문완료) */
            jointShoppingGroupDomainService.changeProductsState(jointShoppingGroup);

            /* 지급대기상태로 생성 */
            Integer provisionCost = max * participationRequest.getPaymentCost();
            ProvisionRecord provisionRecord
                    = provisionRecordDomainService.createProvisionRecord(jointShoppingGroup, provisionCost);

            /* 지급대기상태로 저장 */
            provisionRecordDomainService.saveProvisionRecord(provisionRecord);
        }


        /* 엔티티 반환 */
        return jointShoppingParticipationUser;

    }

    /* 공동구매 참가 취소 */
    @Transactional
    public void deleteParticipation(String userEmail, Long participationNum) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        /* 모임이 참가 모집중인 상태인지 체크*/
        // 모임 찾기
        JointShoppingGroup jointShoppingGroup = jointShoppingParticipationDomainService.findGroup(participationNum);
        // 상태가 모집중이 아니면 취소 불가
        jointShoppingGroupDomainService.checkProductsStateRecruitment(jointShoppingGroup);

        /* 결제 환불 기능 여기에 추가*/

        /* soft delete*/
        jointShoppingParticipationDomainService.deleteParticipation(participationNum);
    }

    /* 공동구매 참가자 물품 배송 정보 등록(수정) */
    @Transactional
    public void updateDeliveryInfo(String userEmail, Long participationNum, JointShoppingDeliveryInfoRequest jointShoppingDeliveryInfoRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        // 모임 찾기
        JointShoppingGroup jointShoppingGroup = jointShoppingParticipationDomainService.findGroup(participationNum);

        jointShoppingParticipationDomainService.updateDeliveryInfo(participationNum, jointShoppingDeliveryInfoRequest, jointShoppingGroup, user);
    }

    /*  공동구매 참가자 물품 수령으로 변경 */
    @Transactional
    public void updateProductsReceipt(String userEmail, Long participationNum) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        /* 물품 수령 여부 */
        jointShoppingParticipationDomainService.updateProductsReceipt(participationNum);

        // 모임 찾기
        JointShoppingGroup jointShoppingGroup = jointShoppingParticipationDomainService.findGroup(participationNum);

        /* 수령시 자동으로 모임 상품 상태 변경(도착) */
        jointShoppingGroupDomainService.changeProductsState(jointShoppingGroup);

        /* 공동구매 참가자 모두가 물품 수령으로 변경했는지 체크 */
        int cnt = jointShoppingParticipationDomainService.findReceiptUserCount(jointShoppingGroup);
        int max = jointShoppingGroup.getJointShoppingParticipationMaximumCount();

        /* 방장에게 전체 비용지급 */
        if (cnt >= max) {
            /* 결제기능 추가 */

            /* 지급완료 처리 후 soft delete */
            provisionRecordDomainService.deleteProvisionRecord(jointShoppingGroup);
            /* 공동구매모임 soft delete */
            jointShoppingGroupDomainService.deleteGroup(jointShoppingGroup.getJointShoppingGroupNum(), user);
        }
    }
}
