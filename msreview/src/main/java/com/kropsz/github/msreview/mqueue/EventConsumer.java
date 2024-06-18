package com.kropsz.github.msreview.mqueue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.github.msreview.exceptions.InvalidEventException;
import com.kropsz.github.msreview.service.ReviewService;
import com.kropsz.github.msreview.web.dto.RecivePayload;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final ReviewService reviewService;

    @RabbitListener(queues = "${mq.queue.event-notification}")
    public void reciveEventReview(@Payload String payload) throws JsonProcessingException {
        try {
            var mapper = new ObjectMapper();
            var review = mapper.readValue(payload, RecivePayload.class);
            reviewService.saveReview(review);
        } catch (RuntimeException e) {
            throw new InvalidEventException("Erro ao salvar o evento.");
        }
    }
}