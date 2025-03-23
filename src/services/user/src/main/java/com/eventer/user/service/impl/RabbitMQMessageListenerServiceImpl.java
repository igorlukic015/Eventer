package com.eventer.user.service.impl;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.cache.data.model.EventCategory;
import com.eventer.user.cache.service.CacheEventCategoryService;
import com.eventer.user.cache.service.CacheEventService;
import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.cache.web.dto.EventDTO;
import com.eventer.user.contracts.ApplicationStatics;
import com.eventer.user.contracts.message.Message;
import com.eventer.user.contracts.message.MessageStatics;
import com.eventer.user.service.MessageListenerService;
import com.eventer.user.service.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RabbitMQMessageListenerServiceImpl implements MessageListenerService {
    private final ObjectMapper objectMapper;
    private final CacheEventService cacheEventService;
    private final CacheEventCategoryService cacheEventCategoryService;
    private final SubscriptionService subscriptionService;

    public RabbitMQMessageListenerServiceImpl(
            ObjectMapper objectMapper,
            CacheEventService cacheEventService,
            CacheEventCategoryService cacheEventCategoryService, SubscriptionService subscriptionService) {
        this.objectMapper = objectMapper;
        this.cacheEventService = cacheEventService;
        this.cacheEventCategoryService = cacheEventCategoryService;
        this.subscriptionService = subscriptionService;
    }

    @RabbitListener(queues = ApplicationStatics.EVENTER_DATA_MESSAGE_QUEUE)
    @Override
    public void receiveMessage(String message) {
        try {
            Message parsed = this.objectMapper.readValue(message, Message.class);

            switch (parsed.getEntityType()) {
                case "Event" -> this.handleEventMessage(parsed);
                case "EventCategory" -> this.handleEventCategoryMessage(parsed);
                default -> throw new RuntimeException("NOT_SUPPORTED_ENTITY");
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void handleEventMessage(Message message) {
        if (Objects.equals(message.getAction(), MessageStatics.ACTION_DELETED)) {
            Long deletedId = this.objectMapper.convertValue(message.getData(), Long.class);
            this.cacheEventService.remove(deletedId);
            return;
        }
        EventDTO eventDTO = this.objectMapper.convertValue(message.getData(), EventDTO.class);

        System.out.println(message.getData());

        Event event =
                new Event(
                        eventDTO.id(),
                        eventDTO.title(),
                        eventDTO.description(),
                        eventDTO.location(),
                        eventDTO.date(),
                        eventDTO.weatherConditions(),
                        eventDTO.categories(),
                        eventDTO.images());

        if (Objects.equals(message.getAction(), MessageStatics.ACTION_CREATED)) {
            this.cacheEventService.add(event);
            this.subscriptionService.sendNewEventNotification(event);
            return;
        }

        this.cacheEventService.update(event);
        this.subscriptionService.sendEventUpdateNotification(event);
    }

    private void handleEventCategoryMessage(Message message) {
        if (Objects.equals(message.getAction(), MessageStatics.ACTION_DELETED)) {
            Long deletedId = this.objectMapper.convertValue(message.getData(), Long.class);
            this.cacheEventCategoryService.remove(deletedId);
            return;
        }
        EventCategoryDTO categoryDTO =
                this.objectMapper.convertValue(message.getData(), EventCategoryDTO.class);

        EventCategory category =
                new EventCategory(categoryDTO.id(), categoryDTO.name(), categoryDTO.description());

        if (Objects.equals(message.getAction(), MessageStatics.ACTION_CREATED)) {
            this.cacheEventCategoryService.add(category);
            return;
        }

        this.cacheEventCategoryService.update(category);
    }
}
