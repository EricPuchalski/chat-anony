package com.chat_chat_anony.model;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ChatMessage {
    private String roomId;
    private String username;
    private String gender;
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;


    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }


}
