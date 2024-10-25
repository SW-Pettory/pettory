package com.pettory.pettory.walkinggroup.command.application.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.exception.UnauthorizedException;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import com.pettory.pettory.walkinggroup.command.application.dto.WalkingGroupCreateRequest;
import com.pettory.pettory.walkinggroup.command.application.dto.WalkingGroupUpdateRequest;
import com.pettory.pettory.walkinggroup.command.domain.aggregate.WalkingGroup;
import com.pettory.pettory.walkinggroup.command.domain.aggregate.WalkingGroupState;
import com.pettory.pettory.walkinggroup.command.domain.repository.WalkingGroupRepository;
import com.pettory.pettory.walkinggroup.command.mapper.WalkingGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalkingGroupCommandService {

    private final WalkingGroupRepository walkingGroupRepository;
    private final UserRepository userRepository;

    public int createWalkingGroup(String userEmail, WalkingGroupCreateRequest walkingGroupRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroup newWalkingGroup = WalkingGroupMapper.toEntity(walkingGroupRequest, user.getUserId());

        WalkingGroup walkingGroup = walkingGroupRepository.save(newWalkingGroup);

        return walkingGroup.getWalkingGroupId();
    }

    @Transactional
    public void updateWalkingGroup(String userEmail, int walkingGroupId, WalkingGroupUpdateRequest walkingGroupRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroup walkingGroup = walkingGroupRepository.findById(walkingGroupId)
                .orElseThrow(() -> new NotFoundException("해당 아이디에 맞는 산책모임이 없습니다. Id : " + walkingGroupId));

        if (walkingGroup.getWalkingGroupOwner() != user.getUserId()) {
            throw new UnauthorizedException("해당 산책 모임을 수정할 권한이 없습니다.");
        }

        walkingGroup.updateWalkingGroupDetails(
                walkingGroupRequest.getWalkingGroupName(),
                walkingGroupRequest.getWalkingGroupInfo(),
                walkingGroupRequest.getWalkingGroupMaximumCount()
        );
    }

    @Transactional
    public void deleteWalkingGroup(String userEmail, int walkingGroupId) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroup walkingGroup = walkingGroupRepository.findById(walkingGroupId)
                .orElseThrow(() -> new NotFoundException("산책 모임을 찾을 수 없습니다."));

        if (walkingGroup.getWalkingGroupOwner() != user.getUserId()) {
            throw new UnauthorizedException("해당 산책 모임을 삭제할 권한이 없습니다.");
        }

        walkingGroupRepository.deleteById(walkingGroupId);

    }
}
