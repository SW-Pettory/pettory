package com.pettory.pettory.walkinggroup.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "산책모임 상세 정보")
// walkingGroupDTO에서 원하는 정보만을 뽑고 새로운 정보를 추가하기 위해
public class WalkingGroupDetailResponse {
    private WalkingGroupDTO walkingGroup;
    private Boolean isLeader;

    public WalkingGroupDetailResponse(WalkingGroupDTO walkingGroup, Boolean isLeader) {
        this.walkingGroup = walkingGroup;
        this.isLeader = isLeader;
    }
}
