package com.pettory.pettory.walkingGroupApplication.command.application.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.walkingGroupApplication.command.application.dto.RegisterWalkingGroupUpdateRequest;
import com.pettory.pettory.walkingGroupApplication.command.application.dto.WalkingGroupApplicationRequest;
import com.pettory.pettory.walkingGroupApplication.command.application.service.RegisterWalkingGroupCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "가입한 산책 모임")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register-walking-group")
public class RegisterWalkingGroupCommandController {

    private final RegisterWalkingGroupCommandService registerWalkingGroupCommandService;

//    @Operation(summary = "승인된 회원", description = "신청이 승인된 회원은 가입된 산책 목록에 산책모임이 등록된다.")
//    @PutMapping("/{walkingGroupApplicationId}/acceptance")
//    public ResponseEntity<CommonResponseDTO> acceptRegisterWalkingGroup(
//            @PathVariable int walkingGroupApplicationId,
//            @RequestBody @Valid WalkingGroupApplicationRequest walkingGroupApplicationRequest
//    ) {
//
//        registerWalkingGroupCommandService.acceptWalkingGroup(walkingGroupApplicationId, walkingGroupApplicationRequest);
//
//        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "가입한 산책 모임 등록 성공", null);
//        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
//
//    }

    @Operation(summary = "가입한산책모임수정", description = "가입한 산책 모임을 수정한다.")
    @PutMapping("/{registerWalkingGroupId}")
    public ResponseEntity<CommonResponseDTO> updateRegisterWalkingGroup(
            @PathVariable int registerWalkingGroupId,
            @RequestBody @Valid RegisterWalkingGroupUpdateRequest registerWalkingGroupUpdateRequest
    ) {

        registerWalkingGroupCommandService.updateRegisterWalkingGroup(
                registerWalkingGroupId, registerWalkingGroupUpdateRequest);

        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "가입한 산책 모임 수정", registerWalkingGroupUpdateRequest);
        return ResponseEntity.ok(successResponse);

    }

    @Operation(summary = "가입한 산책 모임 삭제", description = "가입한 산책 모임을 삭제한다.")
    @DeleteMapping("/{registerWalkingGroupId}")
    public ResponseEntity<CommonResponseDTO> deleteRegisterWalkingGroup(@PathVariable int registerWalkingGroupId) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        registerWalkingGroupCommandService.deleteRegisterWalkingGroup(currentUserEmail, registerWalkingGroupId);

        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "가입한 산책 모임 삭제 성공", null);
        return ResponseEntity.ok(successResponse);

    }

}
