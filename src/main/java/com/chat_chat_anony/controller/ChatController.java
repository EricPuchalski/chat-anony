package com.chat_chat_anony.controller;

import com.chat_chat_anony.model.ChatMessage;
import com.chat_chat_anony.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatRoomService;

    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        messagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
    }

    @MessageMapping("/chat.addUser/{roomId}")
    public void addUser(@DestinationVariable String roomId,
                        @Payload ChatMessage chatMessage,
                        SimpMessageHeaderAccessor headerAccessor) {

        log.info("Received chatMessage: {}", chatMessage); // Verifica qué llega
        log.info("Username: {}", chatMessage.getUsername()); // Verifica el username

        if (chatMessage.getUsername() == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        // Obtener los atributos de la sesión o inicializarlos si son nulos
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null) {
            sessionAttributes = new ConcurrentHashMap<>();
            headerAccessor.setSessionAttributes(sessionAttributes);
        }

        // Add username in web socket session
        sessionAttributes.put("username", chatMessage.getUsername());
        sessionAttributes.put("roomId", roomId);
        sessionAttributes.put("gender", chatMessage.getGender());

        chatRoomService.addUserToRoom(roomId, chatMessage.getUsername());

        chatMessage.setTimestamp(LocalDateTime.now());
        messagingTemplate.convertAndSend("/room/" + roomId, chatMessage);

        log.info("User {} joined room {}", chatMessage.getUsername(), roomId);
    }


    @MessageMapping("/chat.leaveRoom/{roomId}")
    public void leaveRoom(@DestinationVariable String roomId,
                          @Payload ChatMessage chatMessage) {

        chatRoomService.removeUserFromRoom(roomId, chatMessage.getUsername());

        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setType(ChatMessage.MessageType.LEAVE);
        messagingTemplate.convertAndSend("/room/" + roomId, chatMessage);

        log.info("User {} left room {}", chatMessage.getUsername(), roomId);
    }
}
