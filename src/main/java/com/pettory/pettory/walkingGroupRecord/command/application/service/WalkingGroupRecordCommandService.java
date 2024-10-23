package com.pettory.pettory.walkingGroupRecord.command.application.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.exception.UnauthorizedException;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import com.pettory.pettory.walkingGroupRecord.command.application.dto.WalkingGroupRecordUpdateRequest;
import com.pettory.pettory.walkingGroupRecord.command.application.dto.WalkingGroupRecordCreateRequest;
import com.pettory.pettory.walkingGroupRecord.command.domain.aggregate.WalkingGroupRecord;
import com.pettory.pettory.walkingGroupRecord.command.domain.aggregate.WalkingGroupRecordState;
import com.pettory.pettory.walkingGroupRecord.command.domain.repository.WalkingGroupRecordRepository;
import com.pettory.pettory.walkingGroupRecord.command.mapper.WalkingGroupRecordMapper;
import com.pettory.pettory.walkinggroup.command.domain.aggregate.WalkingGroup;
import com.pettory.pettory.walkinggroup.command.domain.repository.WalkingGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalkingGroupRecordCommandService {

    private final WalkingGroupRecordRepository walkingGroupRecordRepository;
    private final UserRepository userRepository;
    private final WalkingGroupRepository walkingGroupRepository;

    @Transactional
    public int createWalkingGroupRecord(String userEmail, WalkingGroupRecordCreateRequest walkingGroupRecordRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroupRecord newWalkingGroupRecord = WalkingGroupRecordMapper.toEntity(walkingGroupRecordRequest);

        WalkingGroup walkingGroup = walkingGroupRepository.findById(newWalkingGroupRecord.getWalkingGroupId())
                .orElseThrow(() -> new NotFoundException("해당 산책 모임을 찾을 수 없습니다."));

        if (walkingGroup.getWalkingGroupOwner() != user.getUserId()) {
            throw new UnauthorizedException("해당 산책 모임 기록을 생성할 권한이 없습니다.");
        }

        WalkingGroupRecord walkingGroupRecord = walkingGroupRecordRepository.save(newWalkingGroupRecord);

        return walkingGroupRecord.getWalkingGroupRecordId();
    }

    @Transactional
    public void updateWalkingGroupRecord(String userEmail, int walkingGroupRecordId, WalkingGroupRecordUpdateRequest walkingGroupRecordRequest) {
        UserSecurity.validateCurrentUser(userEmail);

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroupRecord walkingGroupRecord = walkingGroupRecordRepository.findById(walkingGroupRecordId)
                .orElseThrow(() -> new NotFoundException("해당 아이디에 맞는 산책모임기록이 없습니다. id : " + walkingGroupRecordId));

        WalkingGroup walkingGroup = walkingGroupRepository.findById(walkingGroupRecord.getWalkingGroupId())
                .orElseThrow(() -> new NotFoundException("해당 산책 모임을 찾을 수 없습니다."));

        if (walkingGroup.getWalkingGroupOwner() != user.getUserId()) {
            throw new UnauthorizedException("해당 산책 모임 기록을 생성할 권한이 없습니다.");
        }

        walkingGroupRecord.updateWalkingGroupRecordDetails(
                walkingGroupRecordRequest.getWalkingGroupDate(),
                walkingGroupRecordRequest.getWalkingGroupRecordDuration(),
                walkingGroupRecordRequest.getWalkingGroupRouteStart(),
                walkingGroupRecordRequest.getWalkingGroupRouteEnd(),
                WalkingGroupRecordState.valueOf(walkingGroupRecordRequest.getWalkingGroupRecordState())
        );
    }

    @Transactional
    public void deleteWalkingGroupRecord(String userEmail, int walkingGroupRecordId) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        WalkingGroupRecord walkingGroupRecord = walkingGroupRecordRepository.findById(walkingGroupRecordId)
                .orElseThrow(() -> new NotFoundException("해당 아이디에 맞는 산책모임기록이 없습니다. id : " + walkingGroupRecordId));

        WalkingGroup walkingGroup = walkingGroupRepository.findById(walkingGroupRecord.getWalkingGroupId())
                .orElseThrow(() -> new NotFoundException("해당 산책 모임을 찾을 수 없습니다."));

        if (walkingGroup.getWalkingGroupOwner() != user.getUserId()) {
            throw new UnauthorizedException("해당 산책 모임 기록을 생성할 권한이 없습니다.");
        }

        walkingGroupRecordRepository.deleteById(walkingGroupRecordId);
    }
}
