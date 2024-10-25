package com.pettory.pettory.chat.query.service;

import com.pettory.pettory.chat.query.dto.SelectChattingEntityResponse;
import com.pettory.pettory.chat.query.dto.SelectChattingResponse;
import com.pettory.pettory.chat.query.mapper.ChattingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChattingMapper chattingMapper;

    /* 1. 채팅 조회 */
    public List<SelectChattingResponse> selectChatRoomChatting(Integer chatRoomUniqueNum) {
        return chattingMapper.selectChatRoomChatting(chatRoomUniqueNum);
    }

    public int checkChatroom(Integer chatroomTypeNum) {
        return chattingMapper.checkChatroom(chatroomTypeNum);
    }
}
