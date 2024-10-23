package com.pettory.pettory.chat.command.domain.repository;

import com.pettory.pettory.chat.command.domain.aggregate.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatroom);

    Optional<ChatRoom> findById(int chatroomUniqueNum);

    void deleteById(int chatroomUniqueNum);
}
