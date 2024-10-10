package com.pettory.pettory.chat.query.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatQueryServiceTest {

    @Autowired
    private ChatQueryService chatQueryService;

    @Test
    void selectChatRoomChatting() {
        Assertions.assertDoesNotThrow(
                () -> chatQueryService.selectChatRoomChatting(1)
        );
    }
}