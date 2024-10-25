package com.pettory.pettory.walkingGroupApplication.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "산책모임신청")
public class WalkingGroupApplicationCreateRequest {

    @Schema(description = "산책모임ID", example = "1")
    private int walkingGroupId;

}
