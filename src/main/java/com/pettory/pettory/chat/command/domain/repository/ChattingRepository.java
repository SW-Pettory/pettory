package com.pettory.pettory.chat.command.domain.repository;

import com.pettory.pettory.chat.command.domain.aggregate.Chatting;

import java.util.Optional;

public interface ChattingRepository {
    Chatting save(Chatting chatting);

    Optional<Chatting> findById(int chattingUniqueNum);

    void deleteById(int chattingUniqueNum);
}
