package com.pettory.pettory.jointshopping.command.application.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingDeliveryInfoRequest;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingGroupRequest;
import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingParticipationRequest;
import com.pettory.pettory.jointshopping.command.application.service.JointShoppingParticipationApplicationService;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingParticipationUser;
import com.pettory.pettory.security.util.UserSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Tag(name = "공동구매 참가", description = "공동구매 참가 등록/취소/배송 정보 등록/물품 수령 변경/조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("jointshopping")
public class JointShoppingParticipationCommandController {

    private final JointShoppingParticipationApplicationService jointShoppingParticipationApplicationService;

    @Operation(summary = "공동구매 참가 등록", description = "회원이 공동구매 참가에 등록한다. 참가인원이 가득찼다면 참가할 수 없다.")
    @ApiResponse(responseCode = "201", description = "공동구매 참가 성공")
    @PostMapping("/participation")
    public ResponseEntity<CommonResponseDTO> createParticipation(
            @RequestBody @Valid JointShoppingParticipationRequest participationRequest
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        JointShoppingParticipationUser jointShoppingParticipationUser = jointShoppingParticipationApplicationService.createParticipation(currentUserEmail, participationRequest);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "공동구매 참가 성공", jointShoppingParticipationUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "공동구매 참가 취소", description = "회원이 공동구매 참가 취소한다. 참가방의 인원이 가득차면 취소가 불가능하다.")
    @ApiResponse(responseCode = "204", description = "공동구매 참가 취소 성공")
    @DeleteMapping("/participation/{jointShoppingGroupNum}/users")
    public ResponseEntity<CommonResponseDTO> deleteParticipation(
            @PathVariable  final Long jointShoppingGroupNum
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingParticipationApplicationService.deleteParticipation(currentUserEmail, jointShoppingGroupNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "공동구매 참가 취소 성공", jointShoppingGroupNum);

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @Operation(summary = "공동구매 참가자 물품 배송 정보 등록", description = "공동구매 방장이 참가자에게 물품을 보낼 배송 정보를 등록한다.")
    @ApiResponse(responseCode = "201", description = "공동구매 참가자 물품 배송 정보 등록 성공")
    @PutMapping("/participation/delivery-info/{participationNum}")
    public ResponseEntity<CommonResponseDTO> updateDeliveryInfo(
            @PathVariable @Schema(example = "12") final Long participationNum,
            @RequestBody @Valid JointShoppingDeliveryInfoRequest jointShoppingDeliveryInfoRequest
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingParticipationApplicationService.updateDeliveryInfo(currentUserEmail, participationNum, jointShoppingDeliveryInfoRequest);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "공동구매 참가자 물품 배송 정보 등록 성공", jointShoppingDeliveryInfoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @Operation(summary = "공동구매 참가자 물품 수령으로 변경", description = "참가자가 물품 수령여부를 수령으로 변경한다.")
    @ApiResponse(responseCode = "201", description = "공동구매 참가자 물품 수령으로 변경 성공")
    @PutMapping("/participation/products-receipt/{participationNum}")
    public ResponseEntity<CommonResponseDTO> updateProductsReceipt(
            @PathVariable @Schema(example = "12") final Long participationNum
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        jointShoppingParticipationApplicationService.updateProductsReceipt(currentUserEmail, participationNum);
        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "공동구매 참가자 물품 수령으로 변경 성공", participationNum);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

}
