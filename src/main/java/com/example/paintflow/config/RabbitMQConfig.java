package com.example.paintflow.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // logs Queue (설정 유지)
    @Bean
    Queue logsQueue() {
        return new Queue("logs", true);
    }

    // alarm Queue (설정 유지)
    @Bean
    Queue alarmQueue() {
        return new Queue("alarm", false);
    }

    // Exchange 설정 유지
    @Bean
    FanoutExchange alarmExchange() {
        return new FanoutExchange("alarm", false, false);
    }

    @Bean
    FanoutExchange logsExchange() {
        return new FanoutExchange("logs", false, false);
    }

    // alarmQueue와 alarmExchange 연결 (설정 유지)
    @Bean
    Binding bindAlarmQueue(FanoutExchange alarmExchange, Queue alarmQueue) {
        return BindingBuilder.bind(alarmQueue).to(alarmExchange);
    }

    // logsQueue와 logsExchange 연결 추가
    @Bean
    Binding bindLogsQueue(FanoutExchange logsExchange, Queue logsQueue) {
        return BindingBuilder.bind(logsQueue).to(logsExchange);
    }
}
