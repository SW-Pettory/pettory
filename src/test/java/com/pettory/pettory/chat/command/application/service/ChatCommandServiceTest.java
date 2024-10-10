package com.pettory.pettory.chat.command.application.service;

import com.pettory.pettory.chat.command.application.dto.chatroom.DeleteChatRoomRequest;
import com.pettory.pettory.chat.command.application.dto.chatroom.InsertChatRoomRequest;
import com.pettory.pettory.chat.command.application.dto.chatting.InsertChattingRequest;
import com.pettory.pettory.chat.command.application.dto.chatting.ModifyChattingRequest;
import com.pettory.pettory.chat.command.application.dto.chatting.SoftDeleteChattingRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootTest
class ChatCommandServiceTest {

    @Autowired
    private ChatCommandService chatCommandService;

    private static Stream<Arguments> getChatRoom() {
        return Stream.of(
                Arguments.of(
                        6,
                        "ACTIVE",
                        "WALKING",
                        6
                )
        );
    }

    private static Stream<Arguments> getDeleteChatRoom() {
        return Stream.of(
                Arguments.of(
                        1,
                        "DELETE"
                )
        );
    }

    private static Stream<Arguments> getRegisterChatting() {
        return Stream.of(
                Arguments.of(
                        4,
                        3,
                        "안녕하세요",
                        "ACTIVE",
                        1
                )
        );
    }

    private static Stream<Arguments> getModifyChatting() {
        return Stream.of(
                Arguments.of(
                        15,
                        1,
                        "안녕하세요 수정합니다!",
                        "MODIFY",
                        1
                )
        );
    }

    private static Stream<Arguments> getSoftDeleteChatting() {
        return Stream.of(
                Arguments.of(
                        15,
                        1,
                        "DELETE"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getChatRoom")
    void testRegisterChatRoom(int chatRoomUniqueNum,String chatRoomState,
                              String chatRoomType, int chatRoomTypeNum) {
        InsertChatRoomRequest insertChatRoomRequest = new InsertChatRoomRequest(
                chatRoomUniqueNum,
                LocalDateTime.now(),
                chatRoomState,
                chatRoomType,
                chatRoomTypeNum
        );

        Assertions.assertDoesNotThrow(
                () -> chatCommandService.registerChatRoom(insertChatRoomRequest)
        );
    }

    @ParameterizedTest
    @MethodSource("getDeleteChatRoom")
    void deleteChatRoom(int chatRoomUniqueNum, String chatRoomState) {
        DeleteChatRoomRequest deleteChatRoomRequest = new DeleteChatRoomRequest(
                chatRoomUniqueNum,
                LocalDateTime.now(),
                LocalDateTime.now(),
                chatRoomState
        );

        Assertions.assertDoesNotThrow(
                () -> chatCommandService.deleteChatRoom(deleteChatRoomRequest)
        );
    }

    @ParameterizedTest
    @MethodSource("getRegisterChatting")
    void registerChatting(int chattingUniqueNum, int chattingChatRoomUnique,
                          String chattingContent, String chattingState,
                          int userId) {
        InsertChattingRequest newChatting = new InsertChattingRequest(
                chattingUniqueNum,
                chattingChatRoomUnique,
                chattingContent,
                LocalDateTime.now(),
                chattingState,
                userId
        );

        Assertions.assertDoesNotThrow(
                () -> chatCommandService.registerChatting(newChatting)
        );
    }

    @ParameterizedTest
    @MethodSource("getModifyChatting")
    void modifyChatting(int chattingUniqueNum, int chattingChatRoomUnique,
                        String chattingContent, String chattingState) {
        ModifyChattingRequest modifyChatting = new ModifyChattingRequest(
                chattingUniqueNum,
                chattingChatRoomUnique,
                chattingContent,
                LocalDateTime.now(),
                chattingState
        );

        Assertions.assertDoesNotThrow(
                () -> chatCommandService.modifyChatting(modifyChatting)
        );
    }

    @ParameterizedTest
    @MethodSource("getSoftDeleteChatting")
    void softDeleteChatting(int chattingUniqueNum, int chattingChatRoomUniqueNum,
                            String chattingState) {
        SoftDeleteChattingRequest softDeleteChattingRequest = new SoftDeleteChattingRequest(
                chattingUniqueNum,
                chattingChatRoomUniqueNum,
                LocalDateTime.now(),
                chattingState
        );

        Assertions.assertDoesNotThrow(
                () -> chatCommandService.softDeleteChatting(softDeleteChattingRequest)
        );
    }

    @Test
    void hardDeleteChatting() {
        Assertions.assertDoesNotThrow(
                () -> chatCommandService.hardDeleteChatting(12)
        );
    }
}