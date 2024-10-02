package com.pettory.pettory.jointshopping.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor    // 모든 값을 전달받은 생성자 생성
@Schema(description = "즐겨찾기 요청")
public class BookmarkRequest {

    @NotNull
    private final Long jointShoppingGroupNum;
    @NotNull
    private final Long userId;
}
