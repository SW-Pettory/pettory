package com.pettory.pettory.walkingGroupApplication.query.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.query.dto.UserInfoResponse;
import com.pettory.pettory.user.query.mapper.UserMapper;
import com.pettory.pettory.walkingGroupApplication.query.dto.RegisterWalkingGroupDTO;
import com.pettory.pettory.walkingGroupApplication.query.dto.RegisterWalkingGroupDetailResponse;
import com.pettory.pettory.walkingGroupApplication.query.dto.RegisterWalkingGroupListResponse;
import com.pettory.pettory.walkingGroupApplication.query.dto.WalkingGroupUserListResponse;
import com.pettory.pettory.walkingGroupApplication.query.mapper.RegisterWalkingGroupMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterWalkingGroupQueryService {

    private final RegisterWalkingGroupMapper registerWalkingGroupMapper;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public RegisterWalkingGroupListResponse getRegisterWalkingGroups(Integer page, Integer size, String userEmail) {
        UserSecurity.validateCurrentUser(userEmail);

        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        int offset = (page - 1) * size;
        List<RegisterWalkingGroupDTO> registerWalkingGroups =  registerWalkingGroupMapper.selectRegisterWalkingGroups(
                offset, size, userId
        );

        long totalItems = registerWalkingGroupMapper.countRegisterWalkingGroups(userId);

        return RegisterWalkingGroupListResponse.builder()
                .registerWalkingGroups(registerWalkingGroups)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();

    }

    @Transactional(readOnly = true)
    public RegisterWalkingGroupDetailResponse getRegisterWalkingGroup(String userEmail, int registerWalkingGroupId) {
        RegisterWalkingGroupDTO registerWalkingGroupById = registerWalkingGroupMapper.selectRegisterWalkingGroupById(registerWalkingGroupId);

        UserSecurity.validateCurrentUser(userEmail);

        // 로그인 회원 id 조회
        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        if(registerWalkingGroupById == null) {
            throw new NotFoundException("해당 아이디를 가진 산책 모임을 찾을 수 없습니다. id : " + registerWalkingGroupId);
        }

        Boolean isLeader = false;
        if(userId.equals(registerWalkingGroupById.getUserId())) {
            isLeader = true;
        }

        return RegisterWalkingGroupDetailResponse.builder()
                .registerWalkingGroup(registerWalkingGroupById)
                .isLeader(isLeader)
                .build();
    }

    @Transactional(readOnly = true)
    public WalkingGroupUserListResponse getGroupUsers(String userEmail, Long walkingGroupId) {
        UserSecurity.validateCurrentUser(userEmail);

        List<UserInfoResponse> Users = registerWalkingGroupMapper.selectGroupUsers(walkingGroupId);

        long totalItems = registerWalkingGroupMapper.countGroupUsers(walkingGroupId);

        return WalkingGroupUserListResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .groupUserList(Users)
                .totalItems(totalItems)
                .build();

    }
}
