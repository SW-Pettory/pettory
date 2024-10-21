package com.pettory.pettory.jointshopping.command.domain.service;

import com.pettory.pettory.exception.BadJoinException;
import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingDeliveryInfoRequest;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingParticipationRequest;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingParticipationUser;
import com.pettory.pettory.jointshopping.command.domain.repository.JointShoppingParticipationRepository;
import com.pettory.pettory.jointshopping.command.mapper.JointShoppingParticipationUserMapper;
import com.pettory.pettory.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JointShoppingParticipationDomainService {

    private final JointShoppingParticipationRepository jointShoppingParticipationRepository;

    /* 도메인 객체를 생성하는 로직 */
    public JointShoppingParticipationUser createParticipation(JointShoppingParticipationRequest participationRequest, JointShoppingGroup jointShoppingGroup, User user) {
        /* entity 생성 */
        JointShoppingParticipationUser newJointShoppingParticipationUser
                = JointShoppingParticipationUserMapper.toEntity(participationRequest, jointShoppingGroup, user);

        return newJointShoppingParticipationUser;
    }

    /* 도메인 객체를 저장하는 로직 */
    public JointShoppingParticipationUser saveParticipation(JointShoppingParticipationUser newJointShoppingParticipationUser) {
        return jointShoppingParticipationRepository.save(newJointShoppingParticipationUser);
    }

    /* 도메인 객체를 삭제하는 로직 */
    public void deleteParticipation(Long participationNum) {
        jointShoppingParticipationRepository.deleteById(participationNum);
    }

    /* 배송 정보를 수정하는 로직 */
    public void updateDeliveryInfo(Long participationNum, JointShoppingDeliveryInfoRequest deliveryInfoRequest, JointShoppingGroup jointShoppingGroup, User user) {
        JointShoppingParticipationUser jointShoppingParticipationUser
                = jointShoppingParticipationRepository.findById(participationNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 참가자가 없습니다. code : " + participationNum));

        if(!user.getUserId().equals(jointShoppingGroup.getUser().getUserId()) ){
            throw new BadJoinException("방장이 아니여서 배송 정보를 입력하실 수 없습니다.");
        }

        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingParticipationUser.update(
                deliveryInfoRequest.getCourierCode(),
                deliveryInfoRequest.getInvoiceNum()
        );
    }

    /* 물품 수령 여부를 수정하는 로직 */
    public void updateProductsReceipt(Long participationNum) {
        JointShoppingParticipationUser jointShoppingParticipationUser
                = jointShoppingParticipationRepository.findById(participationNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 참가자가 없습니다. code : " + participationNum));

        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingParticipationUser.changeProductsReceipt();
    }

    /* 현재 참가자 수를 반환하는 로직 */
    public Integer findUserCount(JointShoppingGroup jointShoppingGroup) {
        List<JointShoppingParticipationUser> jointShoppingParticipationUserList = jointShoppingParticipationRepository.findByJointShoppingGroup(jointShoppingGroup);

        return jointShoppingParticipationUserList.size();
    }

    /* 물품 수령한 참가자 수를 반환하는 로직 */
    public Integer findReceiptUserCount(JointShoppingGroup jointShoppingGroup) {
        List<JointShoppingParticipationUser> jointShoppingParticipationUserList = jointShoppingParticipationRepository.findByJointShoppingGroupAndProductsReceiptYnTrue(jointShoppingGroup);

        return jointShoppingParticipationUserList.size();
    }

    /* 가입함 모임 찾기 */
    public JointShoppingGroup findGroup(Long participationNum) {
        JointShoppingParticipationUser jointShoppingParticipationUser
                = jointShoppingParticipationRepository.findById(participationNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 참가자가 없습니다. code : " + participationNum));

        return jointShoppingParticipationUser.getJointShoppingGroup();
    }


}
