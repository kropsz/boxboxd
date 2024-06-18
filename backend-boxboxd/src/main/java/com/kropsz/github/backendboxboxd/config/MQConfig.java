package com.kropsz.github.backendboxboxd.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queue.event-notification}")
    private String eventNotification;

    @Bean
    public Queue queueEvent() {
        return new Queue(eventNotification, true);
    }

}
