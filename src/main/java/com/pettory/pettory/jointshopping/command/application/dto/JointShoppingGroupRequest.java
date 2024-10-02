package com.pettory.pettory.jointshopping.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor    // 모든 값을 전달받은 생성자 생성
@Schema(description = "공동구매모임 요청")
public class JointShoppingGroupRequest {

    @NotBlank
    private final String jointShoppingGroupName;
    @NotBlank
    private final String jointShoppingProducts;
    private final String jointShoppingInfo;
    @NotNull
    private final Integer jointShoppingCost;
    @NotNull
    private final Integer jointShoppingGroupMaximumCount;
    @NotNull
    private final Integer jointShoppingParticipationMaximumCount;
    private final String hostCourierCode;
    private final String hostInvoiceNum;
    @NotNull
    private final Long jointShoppingCategoryNum;
    @NotNull
    private final Long userId;

}
