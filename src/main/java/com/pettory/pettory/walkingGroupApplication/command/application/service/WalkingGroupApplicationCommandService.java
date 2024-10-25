package com.pettory.pettory.walkingGroupApplication.command.application.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.exception.UnauthorizedException;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import com.pettory.pettory.walkingGroupApplication.command.application.dto.WalkingGroupApplicationCreateRequest;
import com.pettory.pettory.walkingGroupApplication.command.application.dto.WalkingGroupApplicationUpdateRequest;
import com.pettory.pettory.walkingGroupApplication.command.domain.aggregate.RegisterWalkingGroup;
import com.pettory.pettory.walkingGroupApplication.command.domain.aggregate.WalkingGroupApplication;
import com.pettory.pettory.walkingGroupApplication.command.domain.aggregate.WalkingGroupApprovalState;
import com.pettory.pettory.walkingGroupApplication.command.domain.repository.WalkingGroupApplicationRepository;
import com.pettory.pettory.walkingGroupApplication.command.mapper.RegisterWalkingGroupMapper;
import com.pettory.pettory.walkingGroupApplication.command.mapper.WalkingGroupApplicationMapper;
import com.pettory.pettory.walkinggroup.command.domain.aggregate.WalkingGroup;
import com.pettory.pettory.walkinggroup.command.domain.repository.WalkingGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalkingGroupApplicationCommandService {

    private final WalkingGroupApplicationRepository walkingGroupApplicationRepository;
    private final UserRepository userRepository;
    private final WalkingGroupRepository walkingGroupRepository;

    @Transactional
    public int createWalkingGroupApplication(String userEmail, WalkingGroupApplicationCreateRequest walkingGroupApplicationRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroupApplication newWalkingGroupApplication = WalkingGroupApplicationMapper.toEntity(walkingGroupApplicationRequest, user.getUserId());

        WalkingGroupApplication walkingGroupApplication = walkingGroupApplicationRepository.save(newWalkingGroupApplication);

        return walkingGroupApplication.getWalkingGroupId();

    }

    @Transactional
    public void updateWalkingGroupApplication(String userEmail, int walkingGroupApplicationId, WalkingGroupApplicationUpdateRequest walkingGroupApplicationRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroupApplication walkingGroupApplication = walkingGroupApplicationRepository.findById(walkingGroupApplicationId)
                .orElseThrow(() -> new NotFoundException("해당 코드에 맞는 산책모임이 없습니다. id : " + walkingGroupApplicationId));

        WalkingGroup walkingGroup = walkingGroupRepository.findById(walkingGroupApplication.getWalkingGroupId())
                .orElseThrow(() -> new NotFoundException("해당 산책 모임을 찾을 수 없습니다."));

        if (walkingGroup.getWalkingGroupOwner() != user.getUserId()) {
            throw new UnauthorizedException("해당 산책 모임 신청을 수정할 권한이 없습니다.");
        }

        walkingGroupApplication.updateWalkingGroupApplicationDetails(
                WalkingGroupApprovalState.valueOf(walkingGroupApplicationRequest.getWalkingGroupApprovalState()));

        if(walkingGroupApplication.getWalkingGroupApprovalState() == WalkingGroupApprovalState.ACCEPT){
            RegisterWalkingGroup newRegisterWalkingGroup = RegisterWalkingGroupMapper.toEntity(walkingGroupApplication);

            walkingGroupApplicationRepository.save(newRegisterWalkingGroup);
        }
    }

    @Transactional
    public void deleteWalkingGroupApplication(String userEmail, int walkingGroupApplicationId) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroupApplication walkingGroupApplication = walkingGroupApplicationRepository.findById(walkingGroupApplicationId)
                .orElseThrow(() -> new NotFoundException("해당 코드에 맞는 산책모임이 없습니다."));

        if (walkingGroupApplication.getUserId() != user.getUserId()) {
            throw new UnauthorizedException("해당 산책 모임 신청을 삭제할 권한이 없습니다.");
        }

        walkingGroupApplicationRepository.deleteById(walkingGroupApplicationId);
    }
}
