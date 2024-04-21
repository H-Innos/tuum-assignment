package com.henrikin.tuum.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.henrikin.tuum.config.RabbitConfig.EXCHANGE_NAME;
import static com.henrikin.tuum.config.RabbitConfig.ROUTING_KEY;

/**
 * Publishes messages to RabbitMQ.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
