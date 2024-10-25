package com.pettory.pettory.walkingGroupApplication.query.dto;

import com.pettory.pettory.user.query.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "산책모임회원 응답")
public class WalkingGroupUserListResponse {
    private List<UserInfoResponse> groupUserList;
    private long totalItems;            // 총 아이템 수
}
