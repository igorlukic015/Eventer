package com.eventer.admin.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Set;


@Entity
@Table(name = "event_category")
public class EventCategory extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_category_seq")
    @SequenceGenerator(name = "event_category_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Event> events;

    public EventCategory() {

    }

    public EventCategory(String createdBy,
                         Instant createdDate,
                         String lastModifiedBy,
                         Instant lastModifiedDate,
                         Long id,
                         String name,
                         String description) {
        super(createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.id = id;
        this.name = name;
        this.description = description;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
