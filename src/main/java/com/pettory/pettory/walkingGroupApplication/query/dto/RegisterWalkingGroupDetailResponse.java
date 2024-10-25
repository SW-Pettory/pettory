package com.pettory.pettory.walkingGroupApplication.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "가입한 산책 모임 상세 정보")
public class RegisterWalkingGroupDetailResponse {

    @Schema(description = "산책 모임 정보")
    private RegisterWalkingGroupDTO registerWalkingGroup;
    private Boolean isLeader;

    public RegisterWalkingGroupDetailResponse(RegisterWalkingGroupDTO registerWalkingGroup, Boolean isLeader) {
        this.registerWalkingGroup = registerWalkingGroup;
        this.isLeader = isLeader;
    }
}
