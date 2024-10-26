package com.pettory.pettory.jointshopping.command.domain.service;

import com.pettory.pettory.exception.BadJoinException;
import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingDeliveryInfoRequest;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingGroupRequest;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingCategory;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.repository.JointShoppingGroupRepository;
import com.pettory.pettory.jointshopping.command.mapper.JointShoppingGroupMapper;
import com.pettory.pettory.jointshopping.util.FileUploadUtils;
import com.pettory.pettory.user.command.domain.aggregate.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JointShoppingGroupDomainService {

    private String IMAGE_URL = "http://localhost:8080/joint-shopping-groupimgs/";
    private String IMAGE_DIR = "src/main/resources/static/jointshoppinggroupimgs/";

    private final JointShoppingGroupRepository jointShoppingGroupRepository;

    /* 도메인 객체를 생성하는 로직 */
    public JointShoppingGroup createGroup(JointShoppingGroupRequest groupRequest, User user, JointShoppingCategory JointShoppingCategory) {

        /* dto to entity */
        JointShoppingGroup newJointShoppingGroup = JointShoppingGroupMapper.toEntity(groupRequest,user, JointShoppingCategory);

        return newJointShoppingGroup;
    }

    /* 도메인 객체를 저장하는 로직 */
    public JointShoppingGroup saveGroup(JointShoppingGroup jointShoppingGroup) {
        return jointShoppingGroupRepository.save(jointShoppingGroup);
    }

    /* 도메인 객체를 수정하는 로직 */
    public JointShoppingGroup updateGroup(Long jointShoppingGroupNum, JointShoppingGroupRequest groupRequest, User user, JointShoppingCategory jointShoppingCategory) {

        JointShoppingGroup jointShoppingGroup = jointShoppingGroupRepository.findById(jointShoppingGroupNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 모임이 없습니다. code : " + jointShoppingGroupNum));

        if(!user.getUserId().equals(jointShoppingGroup.getUser().getUserId()) ){
            throw new BadJoinException("방장이 아니여서 수정하실 수 없습니다.");
        }


        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingGroup.update(
                groupRequest.getJointShoppingGroupName(),
                groupRequest.getJointShoppingProducts(),
                groupRequest.getJointShoppingInfo(),
                groupRequest.getJointShoppingCost(),
                groupRequest.getJointShoppingGroupMaximumCount(),
                groupRequest.getJointShoppingParticipationMaximumCount(),
                jointShoppingCategory,
                user
        );

        return jointShoppingGroup;
    }

    /* 모임 검색 */
    public JointShoppingGroup findGroup(Long jointShoppingGroupNum) {
        return jointShoppingGroupRepository.findById(jointShoppingGroupNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 모임이 없습니다. code : " + jointShoppingGroupNum));

    }

    /* 방 상태를 체크하고 리턴하는 로직 */
    public void checkGroupState(JointShoppingGroup jointShoppingGroup) {
        String state = jointShoppingGroup.getJointShoppingGroupState().toString();
        if (!state.equals("APPLICATION")) {
            throw new BadJoinException("방에 참가하실 수 없습니다.");
        }
    }

    /* 도메인 객체를 삭제하는 로직 */
    public void deleteGroup(Long jointShoppingGroupNum, User user) {
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupRepository.findById(jointShoppingGroupNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 모임이 없습니다. code : " + jointShoppingGroupNum));

        if(!user.getUserId().equals(jointShoppingGroup.getUser().getUserId()) ){
            throw new BadJoinException("방장이 아니여서 삭제하실 수 없습니다.");
        }

        /* soft delete 될 수 있도록 entity에 설정함 */
        jointShoppingGroupRepository.deleteById(jointShoppingGroupNum);
    }

    /* 인원수가 가득 찼을 때 마감 상태로 변경하는 로직 */
    public void updateClosing(Long jointShoppingGroupNum) {
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupRepository.findById(jointShoppingGroupNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 모임이 없습니다. code : " + jointShoppingGroupNum));

        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingGroup.changeClosing();
    }

    /* 나가거나 강퇴됬을 때 신청가능 상태로 변경하는 로직 */
    public void updateApplication(JointShoppingGroup jointShoppingGroup) {
        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingGroup.changeApplication();
    }

    /* 배송 정보를 수정하는 로직 */
    public void updateDeliveryInfo(Long jointShoppingGroupNum, JointShoppingDeliveryInfoRequest jointShoppingDeliveryInfoRequest, User user) {

        JointShoppingGroup jointShoppingGroup = jointShoppingGroupRepository.findById(jointShoppingGroupNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 모임이 없습니다. code : " + jointShoppingGroupNum));

        if(!user.getUserId().equals(jointShoppingGroup.getUser().getUserId()) ){
            throw new BadJoinException("방장이 아니여서 배송 정보를 입력하실 수 없습니다.");
        }

        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingGroup.updateDeliveryInfo(
                jointShoppingDeliveryInfoRequest.getCourierCode(),
                jointShoppingDeliveryInfoRequest.getInvoiceNum()
        );
    }

    /* 상품 상태를 체크하고 모집중 상태가 아니라면 취소가 불가능하게 하는 로직 */
    public void checkProductsStateRecruitment(JointShoppingGroup jointShoppingGroup) {
        String state = jointShoppingGroup.getJointShoppingProductsState().toString();
        if (!state.equals("Recruitment")) {
            throw new BadJoinException("이미 배송이 시작되어 취소할 수 없습니다.");
        }
    }

    /* 상품 상태를 체크하고 주문완료 상태가 아니라면 배송 정보 등록이 불가능하게 하는 로직 */
    public void checkProductsStateOrderCompleted(Long jointShoppingGroupNum) {
        JointShoppingGroup jointShoppingGroup = jointShoppingGroupRepository.findById(jointShoppingGroupNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 모임이 없습니다. code : " + jointShoppingGroupNum));

        String state = jointShoppingGroup.getJointShoppingProductsState().toString();
        if (!state.equals("OrderCompleted")) {
            throw new BadJoinException("아직 배송 정보를 입력하실 수 없습니다.");
        }
    }

    /* 상품 상태를 변경하는 로직 */
    public void changeProductsState(JointShoppingGroup jointShoppingGroup) {
        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingGroup.changeProductsState(
                jointShoppingGroup.getJointShoppingProductsState()
        );
    }


}
