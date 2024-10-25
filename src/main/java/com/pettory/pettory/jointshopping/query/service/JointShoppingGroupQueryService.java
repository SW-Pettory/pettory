package com.pettory.pettory.jointshopping.query.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.query.dto.*;
import com.pettory.pettory.jointshopping.query.mapper.JointShoppingGroupMapper;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.query.dto.UserInfoResponse;
import com.pettory.pettory.user.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JointShoppingGroupQueryService {

    private final JointShoppingGroupMapper jointShoppingGroupMapper;
    private final UserMapper userMapper;

    /* 공동구매모임 목록 조회 */
    @Transactional(readOnly = true)
    public JointShoppingGroupListResponse getGroups(String userEmail, Integer page, Integer size, Long categoryNum, String groupName, String products) {

        UserSecurity.validateCurrentUser(userEmail);

        int offset = (page - 1) * size;
        List<JointShoppingGroupDTO> groups = jointShoppingGroupMapper.selectGroups(offset, size, categoryNum, groupName, products);

        long totalItems = jointShoppingGroupMapper.countGroups(categoryNum, groupName, products);

        return JointShoppingGroupListResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .groupList(groups)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();
    }

    /* 공동구매모임 상세 조회  */
    @Transactional(readOnly = true)
    public JointShoppingGroupDetailResponse getGroup(String userEmail, Long groupNum) {

        UserSecurity.validateCurrentUser(userEmail);

        // 로그인 회원 id 조회
        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        JointShoppingGroupDTO group = jointShoppingGroupMapper.selectGroupByNum(groupNum);

        if (group == null) {
            throw new NotFoundException("해당 코드를 가진 그룹을 찾지 못했습니다. 그룹 코드 : " + groupNum);
        }

        Boolean isMaster = false;
        if(userId.equals(group.getUserId())) {
            isMaster = true;
        }

        return JointShoppingGroupDetailResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .group(group)
                .isMaster(isMaster)
                .build();
    }

    /* 즐겨찾기된 모임 목록 조회 */
    @Transactional(readOnly = true)
    public JointShoppingGroupListResponse getBookmarks(String userEmail, Integer page, Integer size) {

        UserSecurity.validateCurrentUser(userEmail);

        // 로그인 회원 id 조회
        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        int offset = (page - 1) * size;
        List<JointShoppingGroupDTO> groups = jointShoppingGroupMapper.selectBookmarks(offset, size, userId);

        long totalItems = jointShoppingGroupMapper.countBookmarks(userId);

        return JointShoppingGroupListResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .groupList(groups)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();
    }

    /* 현재 공동구매모임의 전체 사용자 목록 조회 */
    @Transactional(readOnly = true)
    public JointShoppingUserListResponse getGroupUsers(String userEmail, Long groupNum) {

        UserSecurity.validateCurrentUser(userEmail);

        List<UserInfoResponse> Users = jointShoppingGroupMapper.selectGroupUsers(groupNum);

        long totalItems = jointShoppingGroupMapper.countGroupUsers(groupNum);

        return JointShoppingUserListResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .groupUserList(Users)
                .totalItems(totalItems)
                .build();
    }

    /* 현재 사용자가 참여한 공동구매모임 목록 조회 */
    @Transactional(readOnly = true)
    public JointShoppingGroupListResponse getUserGroups(Integer page, Integer size, String userEmail) {
        UserSecurity.validateCurrentUser(userEmail);

        // 로그인 회원 id 조회
        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        int offset = (page - 1) * size;
        List<JointShoppingGroupDTO> groups = jointShoppingGroupMapper.selectUserGroups(offset, size, userId);

        long totalItems = jointShoppingGroupMapper.countUserGroups(userId);

        return JointShoppingGroupListResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .groupList(groups)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();
    }

    /*  공동구매 물품 배송 정보 조회(방장) */
    @Transactional(readOnly = true)
    public JointShoppingGroupDeliveryInfoResponse getDeliveryInfo(Long groupNum) {
        JointShoppingGroupDTO group = jointShoppingGroupMapper.selectGroupByNum(groupNum);

        if (group == null) {
            throw new NotFoundException("해당 코드를 가진 그룹을 찾지 못했습니다. 그룹 코드 : " + groupNum);
        }

        return JointShoppingGroupDeliveryInfoResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .hostCourierCode(group.getHostCourierCode())
                .hostInvoiceNum(group.getHostInvoiceNum())
                .build();
    }

    /* 지급기록 조회 */
    @Transactional(readOnly = true)
    public ProvisionRecordResponse getProvisionRecord(Integer page, Integer size, String provisionState) {
        int offset = (page - 1) * size;
        List<ProvisionRecordDTO> provisionRecords = jointShoppingGroupMapper.selectProvisionRecords(offset, size, provisionState);

        long totalItems = jointShoppingGroupMapper.countProvisionRecords(provisionState);

        return ProvisionRecordResponse.builder()    // 이 클래스가 가지고 있는 필드값들이 메서드에 자동완성, 세팅을 여기서 함
                .provisionRecords(provisionRecords)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();
    }



}
