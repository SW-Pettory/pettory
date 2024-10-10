package com.pettory.pettory.chat.query.controller;

import com.pettory.pettory.chat.query.dto.SelectChattingEntityResponse;
import com.pettory.pettory.chat.query.dto.SelectChattingResponse;
import com.pettory.pettory.chat.query.service.ChatQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatQueryController {

    private final ChatQueryService chatQueryService;

    /* 채팅방의 채팅 내용 조회 */
    @Operation(summary = "채팅 조회", description = "특정 채팅방의 채팅 내역을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅 조회 성공")
    })
    @GetMapping("/chatroom-chatting/{chatRoomUniqueNum}")
    public ResponseEntity<SelectChattingEntityResponse> selectChatRoomChatting(@PathVariable Integer chatRoomUniqueNum) {
        /* 응답헤더 설정 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        List<SelectChattingResponse> chattingList = chatQueryService.selectChatRoomChatting(chatRoomUniqueNum);

        /* 응답 데이터 설정 */
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("chattingList", chattingList);
        SelectChattingEntityResponse responseMessage = new SelectChattingEntityResponse(HttpStatus.OK.value(),
                "채팅 조회 성공", responseMap);

        return ResponseEntity.ok().headers(headers).body(responseMessage);
    }

}
