package com.example.paintflow.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebSocketController {

    @MessageMapping("/sendSensorData") 
    @SendTo("/topic/sensor_data")
    public String sendMessage() {
        return "{\"message\":\"Hello from server!\"}";
    }

    // 브라우저에서 메시지 전송 테스트
    @GetMapping("/test-websocket")
    @ResponseBody
    public String testWebSocket() {
        return "<script>" +
                "var socket = new SockJS('http://localhost:8080/ws');" +
                "var stompClient = Stomp.over(socket);" +
                "stompClient.connect({}, function(frame) {" +
                "    console.log('WebSocket STOMP 연결 성공:', frame);" +
                "    stompClient.send('/app/sendSensorData', {}, JSON.stringify({message: 'Hello Server!'}));" +
                "});" +
                "</script>";
    }
}
