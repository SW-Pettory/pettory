package com.pettory.pettory.jointshopping.command.application.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import com.pettory.pettory.jointshopping.command.application.service.JointShoppingCategoryApplicationService;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingCategory;
import com.pettory.pettory.security.util.UserSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공동구매 카테고리", description = "카테고리 등록/수정/삭제/조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("jointshopping")
public class JointShoppingCategoryCommandController {

    private final JointShoppingCategoryApplicationService jointShoppingCategoryApplicationService;

    @Operation(summary = "카테고리 등록", description = "관리자가 카테고리를 등록한다.")
    @ApiResponse(responseCode = "201", description = "카테고리 등록 성공")
    @PostMapping("/categories")
    public ResponseEntity<CommonResponseDTO> createCategory(
            @RequestBody String jointShoppingCategoryTitle
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        JointShoppingCategory jointShoppingCategory = jointShoppingCategoryApplicationService.createCategory(currentUserEmail, jointShoppingCategoryTitle);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "카테고리 등록 성공", jointShoppingCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "카테고리 수정", description = "관리자가 카테고리를 수정한다.")
    @ApiResponse(responseCode = "201", description = "카테고리 수정 성공")
    @PutMapping("/categories/{jointShoppingCategoryNum}")
    public ResponseEntity<CommonResponseDTO> updateCategory(
            @PathVariable @Schema(example = "11") final Long jointShoppingCategoryNum,
            @RequestBody String jointShoppingCategoryTitle
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        JointShoppingCategory jointShoppingCategory =  jointShoppingCategoryApplicationService.updateCategory(currentUserEmail, jointShoppingCategoryNum, jointShoppingCategoryTitle);;
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "카테고리 수정 성공", jointShoppingCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "카테고리 삭제", description = "관리자가 카테고리를 삭제한다.")
    @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공")
    @DeleteMapping("/categories/{jointShoppingCategoryNum}")
    public ResponseEntity<CommonResponseDTO> deleteCategory(
            @PathVariable @Schema(example = "11") final Long jointShoppingCategoryNum
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingCategoryApplicationService.deleteCategory(currentUserEmail, jointShoppingCategoryNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "카테고리 삭제 성공", jointShoppingCategoryNum);

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }
}

