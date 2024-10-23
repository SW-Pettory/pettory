package com.pettory.pettory.chat.command.infrastructure.repository;

import com.pettory.pettory.chat.command.domain.repository.ChatRoomRepository;
import com.pettory.pettory.chat.command.domain.aggregate.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/* 채팅방 Repository 생성 - extends JpaRepository 한 상황으로 기본적인 CRUD 가능 */
public interface JpaChatRoomRepository extends ChatRoomRepository, JpaRepository<ChatRoom, Integer> {

}
