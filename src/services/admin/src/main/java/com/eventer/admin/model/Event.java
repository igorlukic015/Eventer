package com.eventer.admin.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "event")
public class Event extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(name = "event_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "linked_categories",
            joinColumns = @JoinColumn(name = "event_category_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<EventCategory> categories;


    public Event() {
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

    public Set<EventCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<EventCategory> categories) {
        this.categories = categories;
    }
}
