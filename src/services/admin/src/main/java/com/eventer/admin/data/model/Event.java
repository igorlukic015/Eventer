package com.eventer.admin.data.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "event")
public class Event extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(name = "event_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "location", length = 255, nullable = false)
    private String location;

    @Column(name = "weather_condition_availability", length = 255, nullable = false)
    private String weatherConditionAvailability;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Set<Image> images;

    @ManyToMany
    @JoinTable(
            name = "linked_categories",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "event_category_id"))
    private Set<EventCategory> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Event() {}

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeatherConditionAvailability() {
        return weatherConditionAvailability;
    }

    public void setWeatherConditionAvailability(String weatherConditionAvailability) {
        this.weatherConditionAvailability = weatherConditionAvailability;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }
}
