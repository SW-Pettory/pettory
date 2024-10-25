package com.pettory.pettory.walkingGroupApplication.command.application.controller;

import com.pettory.pettory.common.CommonResponseDTO;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.walkingGroupApplication.command.application.dto.WalkingGroupApplicationCreateRequest;
import com.pettory.pettory.walkingGroupApplication.command.application.dto.WalkingGroupApplicationUpdateRequest;
import com.pettory.pettory.walkingGroupApplication.command.application.service.WalkingGroupApplicationCommandService;
import com.pettory.pettory.walkinggroup.command.domain.aggregate.WalkingGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "산책 모임 신청")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/walking-group-application")
public class WalkingGroupApplicationCommandController {

    private final WalkingGroupApplicationCommandService walkingGroupApplicationCommandService;

    @Operation(summary = "산책 모임 신청", description = "산책 모임을 신청한다.")
    @PostMapping("/")
    public ResponseEntity<CommonResponseDTO> createWalkingGroupApplication(
            @RequestBody @Valid WalkingGroupApplicationCreateRequest walkingGroupApplicationRequest
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        int walkingGroupApplicationId = walkingGroupApplicationCommandService.createWalkingGroupApplication(currentUserEmail, walkingGroupApplicationRequest);

        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.CREATED.value(), "산책 모임 신청 성공", walkingGroupApplicationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);

    }

    @Operation(summary = "산책모임신청상태수정", description = "산책 모임의 신청 상태를 수정한다.")
    @PutMapping("/{walkingGroupApplicationId}")
    public ResponseEntity<CommonResponseDTO> updateWalkingGroupApplication(
            @PathVariable int walkingGroupApplicationId,
            @RequestBody @Valid WalkingGroupApplicationUpdateRequest walkingGroupApplicationRequest
    ) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        walkingGroupApplicationCommandService.updateWalkingGroupApplication(currentUserEmail, walkingGroupApplicationId, walkingGroupApplicationRequest);

        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "산책 모임 신청 수정 성공", walkingGroupApplicationRequest);
        return ResponseEntity.ok(successResponse);

    }

    @Operation(summary = "산책모임신청삭제", description = "산책모임신청을 삭제한다.")
    @DeleteMapping("/{walkingGroupApplicationId}")
    public ResponseEntity<CommonResponseDTO> deleteWalkingGroupApplication(@PathVariable final int walkingGroupApplicationId) {
        String currentUserEmail = UserSecurity.getCurrentUserEmail();

        walkingGroupApplicationCommandService.deleteWalkingGroupApplication(currentUserEmail, walkingGroupApplicationId);

        CommonResponseDTO successResponse = new CommonResponseDTO(HttpStatus.OK.value(), "산책 모임 신청 삭제 성공", null);
        return ResponseEntity.ok(successResponse);

    }
}
