package com.eventer.user.data.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "event_subscription")
public class EventSubscription extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_subscription_seq")
    @SequenceGenerator(name = "event_subscription_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "eventId", nullable = false)
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSubscription that = (EventSubscription) o;
        return Objects.equals(id, that.id)
                || (Objects.equals(eventId, that.eventId) && Objects.equals(user, that.user));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, user.getId());
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
