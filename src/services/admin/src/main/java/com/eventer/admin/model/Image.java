package com.eventer.admin.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "image")
public class Image extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_seq")
    @SequenceGenerator(name = "image_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public Image() {
    }

    public Image(String createdBy,
                 Instant createdDate,
                 String lastModifiedBy,
                 Instant lastModifiedDate,
                 Long id,
                 String name,
                 Event event) {
        super(createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.id = id;
        this.name = name;
        this.event = event;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
