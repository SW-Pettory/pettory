package com.pettory.pettory.walkingGroupRecord.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "산책 모임 기록 상세 정보")
public class WalkingGroupRecordDetailResponse {

    private List<WalkingGroupRecordDTO> walkingGroupRecord;
    private int currentPage;
    private int totalPages;
    private long totalItems;

//    public WalkingGroupRecordDetailResponse(List<WalkingGroupRecordDTO> walkingGroupRecord) {
//        this.walkingGroupRecord = walkingGroupRecord;
//    }

}
