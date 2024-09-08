package com.eventer.admin.data.model;

import jakarta.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventCategory that = (EventCategory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public EventCategory() {}

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
