package com.pettory.pettory.jointshopping.command.application.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingDeliveryInfoRequest;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingGroupRequest;
import com.pettory.pettory.jointshopping.command.application.dto.WithdrawalRequest;
import com.pettory.pettory.jointshopping.command.application.service.JointShoppingGroupApplicationService;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroupUser;
import com.pettory.pettory.security.util.UserSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "공동구매모임", description = "공동구매모임 등록/수정/삭제/참가/나가기/강퇴/배송 정보 등록/조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("jointshopping")
public class JointShoppingGroupCommandController {

    private final JointShoppingGroupApplicationService jointShoppingGroupApplicationService;

    @Operation(summary = "공동구매모임 등록", description = "회원이 공동구매모임을 입력한 후 등록한다.")
    @ApiResponse(responseCode = "201", description = "공동구매모임 등록 성공")
    @PostMapping("/groups")
    public ResponseEntity<CommonResponseDTO> createGroup(
            @RequestPart @Valid JointShoppingGroupRequest groupRequest,
            @RequestPart(required = false) MultipartFile productImg
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        JointShoppingGroup jointShoppingGroup = jointShoppingGroupApplicationService.createGroup(currentUserEmail, groupRequest, productImg);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "공동구매모임 등록 성공", jointShoppingGroup);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "공동구매모임 수정", description = "회원이 공동구매모임을 내용을 수정한다.")
    @ApiResponse(responseCode = "201", description = "공동구매모임 수정 성공")
    @PutMapping("/groups/{jointShoppingGroupNum}")
    public ResponseEntity<CommonResponseDTO> updateGroup(
            @PathVariable @Schema(example = "6") final Long jointShoppingGroupNum,
            @RequestPart @Valid JointShoppingGroupRequest groupRequest,
            @RequestPart(required = false) MultipartFile productImg
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        JointShoppingGroup jointShoppingGroup = jointShoppingGroupApplicationService.updateGroup(currentUserEmail, jointShoppingGroupNum, groupRequest, productImg);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "공동구매모임 수정 성공", jointShoppingGroup);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "공동구매모임 삭제", description = "회원이 공동구매모임을 삭제한다.")
    @ApiResponse(responseCode = "204", description = "공동구매모임 삭제 성공")
    @DeleteMapping("/groups/{jointShoppingGroupNum}")
    public ResponseEntity<CommonResponseDTO> deleteGroup(
            @PathVariable @Schema(example = "6") final Long jointShoppingGroupNum
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingGroupApplicationService.deleteGroup(currentUserEmail, jointShoppingGroupNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "공동구매모임 삭제 성공", jointShoppingGroupNum);

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @Operation(summary = "공동구매모임 참가", description = "회원이 공동구매모임에 참가한다. 가득찬 모임이거나 이미 강퇴당한적 있는 모임이라면 참가할 수 없다.")
    @ApiResponse(responseCode = "201", description = "공동구매모임 참가 성공")
    @PostMapping("/groups/{jointShoppingGroupNum}/users")
    public ResponseEntity<CommonResponseDTO> insertGroupUser(
            @PathVariable final Long jointShoppingGroupNum
    ){
        String currentUserEmail = UserSecurity.getCurrentUserEmail();
        System.out.println(currentUserEmail);

        JointShoppingGroupUser jointShoppingGroupUser = jointShoppingGroupApplicationService.insertGroupUser(currentUserEmail, jointShoppingGroupNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "공동구매모임 참가 성공", jointShoppingGroupUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "공동구매모임 나가기", description = "회원이 공동구매모임을 나간다.")
    @ApiResponse(responseCode = "204", description = "공동구매모임 나가기 성공")
    @DeleteMapping("/groups/{jointShoppingGroupNum}/users")
    public ResponseEntity<CommonResponseDTO> exitGroupUser(
            @PathVariable final Long jointShoppingGroupNum
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingGroupApplicationService.exitGroupUser(currentUserEmail, jointShoppingGroupNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "공동구매모임 나가기 성공", jointShoppingGroupNum);

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @Operation(summary = "공동구매모임 강퇴", description = "회원이 공동구매모임에서 강퇴당한다. 해당 모임에 다시 참가할 수 없다.")
    @ApiResponse(responseCode = "204", description = "공동구매모임 강퇴 성공")
    @DeleteMapping("/groups/{jointShoppingGroupNum}/users/withdrawal")
    public ResponseEntity<CommonResponseDTO> withdrawalGroupUser(
            @PathVariable final Long jointShoppingGroupNum,
            @RequestBody WithdrawalRequest withdrawalRequest
            ){
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingGroupApplicationService.withdrawalGroupUser(currentUserEmail, jointShoppingGroupNum, withdrawalRequest.getUserEmail());
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "공동구매모임 강퇴 성공", jointShoppingGroupNum);

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @Operation(summary = "공동구매 방장 물품 배송 정보 등록", description = "회원이 공동구매 물품의 배송정보를 등록한다. 아직 주문완료 상태가 아니라면 입력할 수 없다.")
    @ApiResponse(responseCode = "201", description = "공동구매 방장 물품 배송 정보 성공")
    @PutMapping("/groups/delivery-info/{jointShoppingGroupNum}")
    public ResponseEntity<CommonResponseDTO> updateDeliveryInfo(
            @PathVariable @Schema(example = "7") final Long jointShoppingGroupNum,
            @RequestBody @Valid JointShoppingDeliveryInfoRequest jointShoppingDeliveryInfoRequest
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingGroupApplicationService.updateDeliveryInfo(currentUserEmail, jointShoppingGroupNum, jointShoppingDeliveryInfoRequest);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "공동구매 방장 물품 배송 정보 등록 성공", jointShoppingDeliveryInfoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

}
