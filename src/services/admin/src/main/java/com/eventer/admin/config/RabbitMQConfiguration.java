package com.eventer.admin.config;

import com.eventer.admin.contracts.ApplicationStatics;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue myQueue() {
        return new Queue(ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE, false);
    }
}
