package com.pettory.pettory.chat.command.application.service;

import com.pettory.pettory.chat.command.application.dto.chatroom.DeleteChatRoomRequest;
import com.pettory.pettory.chat.command.application.dto.chatroom.InsertChatRoomRequest;
import com.pettory.pettory.chat.command.application.dto.chatting.InsertChattingRequest;
import com.pettory.pettory.chat.command.application.dto.chatting.ModifyChattingRequest;
import com.pettory.pettory.chat.command.application.dto.chatting.SoftDeleteChattingRequest;
import com.pettory.pettory.chat.command.domain.repository.ChatRoomRepository;
import com.pettory.pettory.chat.command.domain.repository.ChattingRepository;
import com.pettory.pettory.chat.command.domain.aggregate.ChatRoom;
import com.pettory.pettory.chat.command.domain.aggregate.Chatting;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatCommandService {
    private final ModelMapper chatModelMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChattingRepository chattingRepository;

    /* 1. 채팅방 추가 */
    @Transactional
    public ChatRoom registerChatRoom(InsertChatRoomRequest insertChatRoomRequest) {
        return chatRoomRepository.save(chatModelMapper.map(insertChatRoomRequest, ChatRoom.class));
    }

    /* 2. 채팅방 상태 수정 */
    @Transactional
    public void deleteChatRoom(DeleteChatRoomRequest deleteChatRoomRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(deleteChatRoomRequest.getChatroomUniqueNum()).orElseThrow(IllegalArgumentException::new);
        chatRoom.modifyChatRoomState(deleteChatRoomRequest.getChatroomState());
        chatRoom.modifyChatRoomUpdateTime(deleteChatRoomRequest.getChatroomUpdateTime());
        chatRoom.modifyChatRoomDeleteTime(deleteChatRoomRequest.getChatroomDeleteTime());
    }

    /* 3. 채팅 내용 DB 저장 */
    @Transactional
    public void registerChatting(InsertChattingRequest insertChattingRequest) {
        chattingRepository.save(chatModelMapper.map(insertChattingRequest, Chatting.class));
    }

    /* 4. 채팅 내용 수정 */
    @Transactional
    public Chatting modifyChatting(ModifyChattingRequest modifyChattingRequest) {
        Chatting chatting = chattingRepository.findById(modifyChattingRequest.getChattingUniqueNum()).orElseThrow(IllegalArgumentException::new);
        /* 채팅 수정 메소드 */
        chatting.modifyChatting(modifyChattingRequest.getChattingContent(),
                                modifyChattingRequest.getChattingUpdateTime(),
                                modifyChattingRequest.getChattingState());
        return chatting;
    }

    /* 5-1. 채팅 얇은 삭제 */
    @Transactional
    public void softDeleteChatting(SoftDeleteChattingRequest softDeleteChattingRequest) {
        Chatting chatting = chattingRepository.findById(softDeleteChattingRequest.getChattingUniqueNum()).orElseThrow(IllegalArgumentException::new);
        /* 채팅 얇은 삭제 기능 메소드 : 상태 변경, deleteTime 삽입 */
        chatting.softDeleteChatting(softDeleteChattingRequest.getChattingState(),
                                    softDeleteChattingRequest.getChattingDeleteTime());
    }

    /* 5-2. 채팅 리얼 삭제 */
    @Transactional
    public void hardDeleteChatting(Integer chattingUniqueNum) {
        chattingRepository.deleteById(chattingUniqueNum);
    }
}
