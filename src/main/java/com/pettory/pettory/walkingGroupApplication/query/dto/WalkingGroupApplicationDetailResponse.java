package com.pettory.pettory.walkingGroupApplication.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "산책 모임 신청 상세 정보")
public class WalkingGroupApplicationDetailResponse {
    private WalkingGroupApplicationDTO walkingGroupApplicationById;

    public WalkingGroupApplicationDetailResponse(WalkingGroupApplicationDTO walkingGroupApplicationById) {
        this.walkingGroupApplicationById = walkingGroupApplicationById;
    }
}
