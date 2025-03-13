package com.example.paintflow.service;

import java.nio.charset.StandardCharsets;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RabbitMQListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "alarm")
    public void receiveMessage(Message message) {
        String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        String[] parts = receivedMessage.split(", ");

        if (parts.length == 5) {
            String timestamp = parts[0];
            String sensorName = parts[1];
            double value1 = Double.parseDouble(parts[2]); // 실수형 지원
            double value2 = Double.parseDouble(parts[3]);
            String status = parts[4];

            // JSON 형식으로 데이터 전송
            SensorData data = new SensorData(timestamp, sensorName, value1, value2, status);
            messagingTemplate.convertAndSend("/topic/sensorData", data);
        } else {
            System.err.println("Invalid message format: " + receivedMessage);
        }
    }

    // 내부 클래스 (JSON 변환을 위해 사용)
    static class SensorData {
        private String timestamp;
        private String sensorName;
        private double value1;
        private double value2;
        private String status;

        public SensorData(String timestamp, String sensorName, double value1, double value2, String status) {
            this.timestamp = timestamp;
            this.sensorName = sensorName;
            this.value1 = value1;
            this.value2 = value2;
            this.status = status;
        }

        public String getTimestamp() { return timestamp; }
        public String getSensorName() { return sensorName; }
        public double getValue1() { return value1; }
        public double getValue2() { return value2; }
        public String getStatus() { return status; }
    }
}
