package com.pettory.pettory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pettory.pettory.chat.command.application.dto.chatting.InsertChattingRequest;
import com.pettory.pettory.chat.command.application.service.ChatCommandService;
import com.pettory.pettory.chat.command.domain.aggregate.Chatting;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.*;

/* WebSocket 핸들러 생성
 * @Component 를 통한 Bean 등록 */
@Component
public class ChatHandler extends TextWebSocketHandler {

    private final ChatCommandService chatCommandService;

    /* JSON -> 객체
     * 객체 -> JSON 변환해주는 Mapper */
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatHandler(ChatCommandService chatCommandService, ObjectMapper objectMapper) {
        this.chatCommandService = chatCommandService;
        this.objectMapper = objectMapper;
    }

    /* 1. 채팅방에 들어와있는 session 들을 추적할 list 생성 */
    private static final List<WebSocketSession> userList = new ArrayList<>();

    /* 1-1. 1번은 하나의 List 에 모든 세션을 담아서 추적 관리한다면...
     * 채팅방 별로 List 를 만들어서 채팅방 별 세션을 관리하면 된다.
     * Integer : 채팅방의 고유번호
     * List<WebSocketSession>> : 채팅방에 담을 세션 */
    private static final Map<Integer, List<WebSocketSession>> chatUserList = new HashMap<>();

    /* 2. 3개의 메소드 오버라이드
     * handleTextMessage
     * afterConnectionEstablished
     * afterConnectionClosed */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session,@NonNull TextMessage message) throws Exception {
        /* getPayload : 전송되는 데이터를 뜻함. - JSON 으로 받아옴. */
        String payload = message.getPayload();
        System.out.println(payload);

        List<WebSocketSession> sessionsInRoom = chatUserList.get(getChatroomUniqueNum(session));
        /* 전송해주기 전 서버 시간을 기록 */
        // 1. 들어온 JSON 데이터 InsertChattingRequest 객체로 변환
        InsertChattingRequest chattingMessage = objectMapper.readValue(payload, InsertChattingRequest.class);

        // 2. 서버의 현재 시간 작성
        chattingMessage.setChattingInsertTime(LocalDateTime.now());

        // 3. DB 채팅 저장
        Chatting chatting = chatCommandService.registerChatting(chattingMessage);

        // 3. JSON 으로 직렬화하여 클라이언트에 전송할 채팅 준비
        String responsePayload = objectMapper.writeValueAsString(chatting);

        /* 채팅방에 들어와있는 모든 Session 에게 메시지 전달 */
        if (sessionsInRoom != null) {
            for (WebSocketSession wsSession : sessionsInRoom) {
                wsSession.sendMessage(new TextMessage(responsePayload));
            }
        }
    }

    /* 연결이 되었을 때 채팅방 session 추적을 위한 list 에 해당 session 추가 */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        System.out.println(getChatroomUniqueNum(session));

        chatUserList.computeIfAbsent(getChatroomUniqueNum(session), k -> userList).add(session);

//        userList.add(session);
    }

    /* 연결이 끊겼을 때 채팅방 session 추적을 위한 list 에서 해당 session 삭제 */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        userList.remove(session);
    }

    private int getChatroomUniqueNum(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        return Integer.parseInt(uri.split("ws/chatroom/")[1]);
    }
}
