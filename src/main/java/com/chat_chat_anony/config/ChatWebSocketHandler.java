package com.chat_chat_anony.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Aquí puedes manejar los mensajes entrantes
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        // Lógica para manejar el mensaje, como enviarlo a otros usuarios en la misma sala
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Lógica para manejar la conexión establecida
        System.out.println("New WebSocket connection established: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Lógica para manejar el cierre de la conexión
        System.out.println("WebSocket connection closed: " + session.getId());
    }
}