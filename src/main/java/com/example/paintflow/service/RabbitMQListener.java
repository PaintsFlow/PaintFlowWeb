package com.example.paintflow.service;

import java.nio.charset.StandardCharsets;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RabbitMQListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RabbitMQListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 알람 메시지 처리
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

    // 센서 데이터 메시지 처리
    @RabbitListener(queues = "logs")  // ✅ "logs" 큐 사용
    public void receiveSensorDataMessage(Message message) {
        try {
            String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("RabbitMQ에서 수신한 메시지: " + receivedMessage);

            String[] parts = receivedMessage.split(", ");
            if (parts.length == 10) {  // ✅ sensorName 제거 후 길이 변경
                String timestamp = parts[0];
                double waterLevel = Double.parseDouble(parts[1]);
                double viscosity = Double.parseDouble(parts[2]);
                double ph = Double.parseDouble(parts[3]);
                double current = Double.parseDouble(parts[4]);
                double voltage = Double.parseDouble(parts[5]);
                double temperature = Double.parseDouble(parts[6]);
                double humidity = Double.parseDouble(parts[7]);
                double paintAmount = Double.parseDouble(parts[8]);
                double pressure = Double.parseDouble(parts[9]);

                SensorChartData sensorData = new SensorChartData(timestamp, waterLevel, viscosity, ph, current, voltage, 
                                                       temperature, humidity, paintAmount, pressure);

                // ✅ JSON 변환
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(sensorData);

                System.out.println("WebSocket으로 전송할 JSON 데이터: " + jsonMessage);

                // ✅ JSON 형식으로 WebSocket 전송
                messagingTemplate.convertAndSend("/topic/sensorDataUpdate", jsonMessage);
                System.out.println("WebSocket으로 데이터 전송 완료!");

            } else {
                System.err.println("Invalid sensor data message format: " + receivedMessage);
            }
        } catch (Exception e) {
            System.err.println("Error processing RabbitMQ message: " + e.getMessage());
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

    //차트에 보낼 내부 클래스
    public static class SensorChartData {
        private String timestamp;
        private double waterLevel;
        private double viscosity;
        private double ph;
        private double current;
        private double voltage;
        private double temperature;
        private double humidity;
        private double paintAmount;
        private double pressure;

        public SensorChartData(String timestamp, double waterLevel, double viscosity, double ph, 
                          double current, double voltage, double temperature, double humidity, 
                          double paintAmount, double pressure) {
            this.timestamp = timestamp;
            this.waterLevel = waterLevel;
            this.viscosity = viscosity;
            this.ph = ph;
            this.current = current;
            this.voltage = voltage;
            this.temperature = temperature;
            this.humidity = humidity;
            this.paintAmount = paintAmount;
            this.pressure = pressure;
        }

        // ✅ Jackson이 JSON 변환을 위해 getter 필요
        public String getTimestamp() { return timestamp; }
        public double getWaterLevel() { return waterLevel; }
        public double getViscosity() { return viscosity; }
        public double getPh() { return ph; }
        public double getCurrent() { return current; }
        public double getVoltage() { return voltage; }
        public double getTemperature() { return temperature; }
        public double getHumidity() { return humidity; }
        public double getPaintAmount() { return paintAmount; }
        public double getPressure() { return pressure; }
    }
}
