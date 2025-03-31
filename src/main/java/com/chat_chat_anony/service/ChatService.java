package com.chat_chat_anony.service;

import com.chat_chat_anony.model.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ChatService {
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new ConcurrentHashMap<>();
    }

    public ChatRoom getChatRoom(String roomId) {
        return chatRooms.computeIfAbsent(roomId, id -> {
            log.info("Creating new chat room: {}", id);
            return ChatRoom.builder()
                    .roomId(id)
                    .createdAt(LocalDateTime.now())
                    .build();
        });
    }

    public void addUserToRoom(String roomId, String username) {
        ChatRoom room = getChatRoom(roomId);
        room.getUsers().add(username);
        log.info("User {} joined room {}", username, roomId);
    }

    public void removeUserFromRoom(String roomId, String username) {
        Optional.ofNullable(chatRooms.get(roomId)).ifPresent(room -> {
            room.getUsers().remove(username);
            log.info("User {} left room {}", username, roomId);

            if (room.getUsers().isEmpty()) {
                chatRooms.remove(roomId);
                log.info("Room {} deleted as it became empty", roomId);
            }
        });
    }

    public boolean roomExists(String roomId) {
        return chatRooms.containsKey(roomId);
    }

    public int getUserCountInRoom(String roomId) {
        return Optional.ofNullable(chatRooms.get(roomId))
                .map(room -> room.getUsers().size())
                .orElse(0);
    }
}