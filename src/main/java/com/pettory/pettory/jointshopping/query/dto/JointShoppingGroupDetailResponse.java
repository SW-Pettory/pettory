package com.pettory.pettory.jointshopping.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "공동구매모임 상세 응답")
public class JointShoppingGroupDetailResponse {
    private JointShoppingGroupDTO group;
    private Boolean isMaster;
}
