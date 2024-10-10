package com.pettory.pettory.chat.command.infrastructure.repository;

import com.pettory.pettory.chat.command.domain.repository.ChattingRepository;
import com.pettory.pettory.chat.command.domain.aggregate.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

/* 채팅 Repository 생성 - extends JpaRepository 한 상황으로 기본적인 CRUD 가능 */
public interface JpaChattingRepository extends ChattingRepository, JpaRepository<Chatting, Integer> {

}
