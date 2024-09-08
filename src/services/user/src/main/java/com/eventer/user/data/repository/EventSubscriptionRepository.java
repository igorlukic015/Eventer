package com.eventer.user.data.repository;

import com.eventer.user.data.model.EventSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, Long> {
    Set<EventSubscription> findAllByEventId(Long eventId);

    Optional<EventSubscription> findByEventIdAndUserId(Long eventId, Long id);

    Set<EventSubscription> findAllByUserId(Long id);
}
