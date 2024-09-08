package com.eventer.user.service.impl;

import com.eventer.user.cache.data.model.Event;
import com.eventer.user.cache.web.dto.EventCategoryDTO;
import com.eventer.user.data.model.CategorySubscription;
import com.eventer.user.data.model.EventSubscription;
import com.eventer.user.data.model.User;
import com.eventer.user.data.repository.CategorySubscriptionRepository;
import com.eventer.user.data.repository.EventSubscriptionRepository;
import com.eventer.user.data.repository.UserRepository;
import com.eventer.user.security.contracts.CustomUserDetails;
import com.eventer.user.service.EmailService;
import com.eventer.user.service.SubscriptionService;
import com.eventer.user.utils.ResultErrorMessages;
import com.github.igorlukic015.resulter.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final EventSubscriptionRepository eventSubscriptionRepository;
    private final CategorySubscriptionRepository categorySubscriptionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public SubscriptionServiceImpl(
            EventSubscriptionRepository eventSubscriptionRepository,
            CategorySubscriptionRepository categorySubscriptionRepository, UserRepository userRepository, EmailService emailService) {
        this.eventSubscriptionRepository = eventSubscriptionRepository;
        this.categorySubscriptionRepository = categorySubscriptionRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void sendNewEventNotification(Event event) {
        Set<CategorySubscription> subscriptions =
                this.categorySubscriptionRepository.findAllByCategoryId(
                        event.getCategories().stream()
                                .map(EventCategoryDTO::id)
                                .collect(Collectors.toSet()));

        Set<String> recipientEmails =
                subscriptions.stream()
                        .map(s -> s.getUser().getUsername())
                        .collect(Collectors.toSet());

        if (!recipientEmails.isEmpty()) {
            this.emailService.sendNewEventNotification(recipientEmails, event);
        }
    }

    public void sendEventUpdateNotification(Event event) {
        Set<EventSubscription> subscriptions =
                this.eventSubscriptionRepository.findAllByEventId(event.getEventId());

        Set<String> recipientEmails =
                subscriptions.stream()
                        .map(s -> s.getUser().getUsername())
                        .collect(Collectors.toSet());

        if (!recipientEmails.isEmpty()) {
            this.emailService.sendEventUpdateNotification(recipientEmails, event);
        }
    }

    @Override
    @Transactional
    public Result create(String entityType, Long entityId, CustomUserDetails userDetails) {
        if (userDetails == null) {
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        String principalUsername = userDetails.getUsername();

        Optional<User> foundUser =
                this.userRepository.findByUsername(principalUsername);

        if (foundUser.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        if(Objects.equals(entityType, Event.class.getSimpleName())) {
            Optional<EventSubscription> existingSubscription = this.eventSubscriptionRepository.findByEventIdAndUserId(entityId, foundUser.get().getId());

            if (existingSubscription.isPresent()) {
                this.eventSubscriptionRepository.delete(existingSubscription.get());
                return Result.success();
            }

            EventSubscription subscription = new EventSubscription();
            subscription.setEventId(entityId);
            subscription.setUser(foundUser.get());

            this.eventSubscriptionRepository.save(subscription);

            return Result.success();
        }

        Optional<CategorySubscription> existingSubscription = this.categorySubscriptionRepository.findByCategoryIdAndUserId(entityId, foundUser.get().getId());

        if (existingSubscription.isPresent()) {
            this.categorySubscriptionRepository.delete(existingSubscription.get());
            return Result.success();
        }

        CategorySubscription subscription = new CategorySubscription();
        subscription.setCategoryId(entityId);
        subscription.setUser(foundUser.get());
        this.categorySubscriptionRepository.save(subscription);

        return Result.success();
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Boolean> getIsEventSubscribed(Long eventId, CustomUserDetails userDetails) {
        if (userDetails == null) {
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        String principalUsername = userDetails.getUsername();

        Optional<User> foundUser =
                this.userRepository.findByUsername(principalUsername);

        if (foundUser.isEmpty()) {
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        Optional<EventSubscription> foundSubscription = this.eventSubscriptionRepository.findByEventIdAndUserId(eventId, foundUser.get().getId());
        return Result.success(foundSubscription.isPresent());
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Set<Long>> getSubscribedCategoryIds(CustomUserDetails userDetails) {
        if (userDetails == null) {
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        String principalUsername = userDetails.getUsername();

        Optional<User> foundUser =
                this.userRepository.findByUsername(principalUsername);

        if (foundUser.isEmpty()) {
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        Set<CategorySubscription> foundSubscriptions = this.categorySubscriptionRepository.findAllByUserId(foundUser.get().getId());

        Set<Long> ids = foundSubscriptions.stream().map(CategorySubscription::getCategoryId).collect(Collectors.toSet());

        return Result.success(ids);
    }
}
