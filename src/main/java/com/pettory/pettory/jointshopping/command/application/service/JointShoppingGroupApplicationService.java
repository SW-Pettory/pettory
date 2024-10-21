package com.pettory.pettory.jointshopping.command.application.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingDeliveryInfoRequest;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingGroupRequest;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingCategory;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroupUser;
import com.pettory.pettory.jointshopping.command.domain.service.JointShoppingCategoryDomainService;
import com.pettory.pettory.jointshopping.command.domain.service.JointShoppingGroupDomainService;
import com.pettory.pettory.jointshopping.command.domain.service.JointShoppingGroupUserDomainService;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class JointShoppingGroupApplicationService {

    private final JointShoppingGroupDomainService jointShoppingGroupDomainService;
    private final JointShoppingGroupUserDomainService jointShoppingGroupUserDomainService;
    private final JointShoppingCategoryDomainService jointShoppingCategoryDomainService;
    private final UserRepository userRepository;

    /* 공동구매모임 등록 */
    @Transactional
    public JointShoppingGroup createGroup(String userEmail, JointShoppingGroupRequest groupRequest, MultipartFile productImg) {

        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        JointShoppingCategory jointShoppingCategory = jointShoppingCategoryDomainService.findCategory(groupRequest.getJointShoppingCategory());

        /* jointshoppinggroup 도메인 생성 로직 실행, entity 반환 */
        JointShoppingGroup newJointShoppingGroup = jointShoppingGroupDomainService.createGroup(groupRequest, productImg, user, jointShoppingCategory);

        /* save 로직 실행 */
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupDomainService.saveGroup(newJointShoppingGroup);

        /* 엔티티 반환 */
        return jointShoppingGroup;
    }

    /* 공동구매모임 수정 */
    @Transactional
    public JointShoppingGroup updateGroup(String userEmail, Long jointShoppingGroupNum, JointShoppingGroupRequest groupRequest, MultipartFile productImg) {

        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        JointShoppingCategory jointShoppingCategory = jointShoppingCategoryDomainService.findCategory(groupRequest.getJointShoppingCategory());

        JointShoppingGroup jointShoppingGroup =  jointShoppingGroupDomainService.updateGroup(jointShoppingGroupNum, groupRequest, productImg, user, jointShoppingCategory);
        return jointShoppingGroup;
    }

    /* 공동구매모임 삭제 */
    @Transactional
    public void deleteGroup(String userEmail, Long jointShoppingGroupNum) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        jointShoppingGroupDomainService.deleteGroup(jointShoppingGroupNum, user);
    }

    /* 공동구매모임 참가(모임 사용자 등록) */
    @Transactional
    public JointShoppingGroupUser insertGroupUser(String userEmail, Long jointShoppingGroupNum) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        // 모임 찾기
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupDomainService.findGroup(jointShoppingGroupNum);

        /* 모임이 신청 가능한 상태인지 체크*/
        jointShoppingGroupDomainService.checkGroupState(jointShoppingGroup);

        /* 해당 방에서 강퇴당한 적이 있는지 체크 */
        jointShoppingGroupUserDomainService.checkResignYn(jointShoppingGroup, user);

        /* JointShoppingGroupUser 도메인 생성 로직 실행, entity 반환 */
        JointShoppingGroupUser newJointShoppingGroupUser = jointShoppingGroupUserDomainService.createGroupUser(jointShoppingGroup, user);

        /* save 로직 실행 */
        JointShoppingGroupUser jointShoppingGroupUser = jointShoppingGroupUserDomainService.saveGroupUser(newJointShoppingGroupUser);

        /* 공동구매모임 자동마감 체크 */
        // 최대 사용자 수를 불러옴
        int max = jointShoppingGroup.getJointShoppingGroupMaximumCount();
        // 현재 사용자 수를 불러옴
        int now = jointShoppingGroupUserDomainService.findUserCount(jointShoppingGroup);
        if (now >= max) {
            jointShoppingGroupDomainService.updateClosing(jointShoppingGroupNum);
        }

        /* 엔티티 반환 */
        return jointShoppingGroupUser;
    }

    /* 공동구매모임 나가기(모임 사용자 삭제) */
    @Transactional
    public void exitGroupUser(String userEmail, Long jointShoppingGroupUserNum) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        /* 공동구매모임 자동신청가능 */
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupUserDomainService.findGroup(jointShoppingGroupUserNum);
        if(jointShoppingGroup.getJointShoppingGroupState().equals("CLOSE")){
            jointShoppingGroupDomainService.updateApplication(jointShoppingGroup);
        }

        jointShoppingGroupUserDomainService.deleteGroupUser(jointShoppingGroupUserNum);
    }

    /* 공동구매모임 강퇴 (모임 사용자 삭제, 재등록 불가 ) */
    @Transactional
    public void withdrawalGroupUser(String userEmail, Long jointShoppingGroupUserNum) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        /* 공동구매모임 자동신청가능 */
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupUserDomainService.findGroup(jointShoppingGroupUserNum);
        if(jointShoppingGroup.getJointShoppingGroupState().equals("CLOSE")){
            jointShoppingGroupDomainService.updateApplication(jointShoppingGroup);
        }

        // 강퇴 여부 수정
        JointShoppingGroupUser newJointShoppingGroupUser = jointShoppingGroupUserDomainService.updateResignYn(jointShoppingGroupUserNum);

        /* saveAndFlush 로직 실행 */
        jointShoppingGroupUserDomainService.saveAndFlushGroupUser(newJointShoppingGroupUser);

        /* soft delete */
        jointShoppingGroupUserDomainService.deleteGroupUser(jointShoppingGroupUserNum);
    }

    /* 공동구매 방장 물품 배송 정보 등록(수정) */
    @Transactional
    public void updateDeliveryInfo(String userEmail, Long jointShoppingGroupNum, JointShoppingDeliveryInfoRequest jointShoppingDeliveryInfoRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        JointShoppingGroup jointShoppingGroup = jointShoppingGroupDomainService.findGroup(jointShoppingGroupNum);

        /* 주문완료 상태에서만 동작 가능 */
        jointShoppingGroupDomainService.checkProductsStateOrderCompleted(jointShoppingGroupNum);

        jointShoppingGroupDomainService.updateDeliveryInfo(jointShoppingGroupNum,jointShoppingDeliveryInfoRequest, user);

        /* 등록시 모임 상품 상태 변경(배송중) */
        jointShoppingGroupDomainService.changeProductsState(jointShoppingGroup);
    }


}
