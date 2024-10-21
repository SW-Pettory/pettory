package com.pettory.pettory.jointshopping.command.application.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import com.pettory.pettory.jointshopping.command.application.service.BookmarkApplicationService;
import com.pettory.pettory.jointshopping.command.domain.aggregate.Bookmark;
import com.pettory.pettory.security.util.UserSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "즐겨찾기", description = "즐겨찾기 등록/삭제/조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("jointshopping")
public class BookmarkCommandController {

    private final BookmarkApplicationService bookmarkApplicationService;

    @Operation(summary = "즐겨찾기 등록", description = "회원이 공동구매모임을 즐겨찾기를 등록한다.")
    @ApiResponse(responseCode = "201", description = "즐겨찾기 등록 성공")
    @PostMapping("/bookmarks")
    public ResponseEntity<CommonResponseDTO> createBookmark(
            @RequestBody Long jointShoppingGroupNum
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        Bookmark bookmark = bookmarkApplicationService.createBookmark(currentUserEmail, jointShoppingGroupNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "즐겨찾기 등록 성공", bookmark);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "즐겨찾기 삭제", description = "회원이 공동구매모임을 즐겨찾기를 취소한다.")
    @ApiResponse(responseCode = "204", description = "즐겨찾기 삭제 성공")
    @DeleteMapping("/bookmarks/{bookmarkNum}")
    public ResponseEntity<CommonResponseDTO> deleteBookmark(
            @PathVariable @Schema(example = "3") final Long bookmarkNum
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        bookmarkApplicationService.deleteBookmark(currentUserEmail, bookmarkNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "즐겨찾기 삭제 성공", bookmarkNum);

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }
}
