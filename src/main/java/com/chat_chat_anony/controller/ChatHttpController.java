package com.chat_chat_anony.controller;

import com.chat_chat_anony.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/users")
@RequiredArgsConstructor
public class ChatHttpController {
    private final ChatService chatRoomService;

    @GetMapping("/room/{roomId}/user-count")
    public int getUserCountInRoom(@PathVariable String roomId) {
        return chatRoomService.getUserCountInRoom(roomId);
    }
}
