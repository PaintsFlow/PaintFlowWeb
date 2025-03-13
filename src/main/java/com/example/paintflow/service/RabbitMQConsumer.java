package com.example.paintflow.service;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private final MessageConverter messageConverter;

    // Jackson2JsonMessageConverter 대신 SimpleMessageConverter 사용
    public RabbitMQConsumer() {
        this.messageConverter = new SimpleMessageConverter();
    }

    // 메시지 수신 처리 메서드
    public void receiveMessage(byte[] message) {
        String convertedMessage = (String) messageConverter.fromMessage(new org.springframework.amqp.core.Message(message));
        System.out.println("Received message: " + convertedMessage);
    }
}
