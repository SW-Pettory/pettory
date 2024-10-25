package com.pettory.pettory.jointshopping.query.dto;

import com.pettory.pettory.user.query.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "공동구매모임회원 응답")
public class JointShoppingUserListResponse {
    private List<UserInfoResponse> groupUserList;
    private long totalItems;            // 총 아이템 수
}
