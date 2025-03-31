package com.chat_chat_anony.listener;

import com.chat_chat_anony.model.ChatMessage;
import com.chat_chat_anony.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatRoomService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String gender = (String) headerAccessor.getSessionAttributes().get("gender");

        if (username != null && roomId != null) {
            log.info("User Disconnected: {}", username);

            ChatMessage chatMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .username(username)
                    .gender(gender)
                    .roomId(roomId)
                    .content(username + " left!")
                    .timestamp(LocalDateTime.now())
                    .build();

            chatRoomService.removeUserFromRoom(roomId, username);
            messagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
        }
    }
}
