package com.kropsz.github.backendboxboxd.publisher;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.github.backendboxboxd.web.dtos.review.ReviewPayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEventNotification;

    public void sendReview(ReviewPayload review) throws JsonProcessingException {
        var json = converIntoJson(review);
        rabbitTemplate.convertAndSend(queueEventNotification.getName(), json);
    }

    public String converIntoJson(ReviewPayload event) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(event);

    }

}
