package com.eventer.admin.data.model;

import jakarta.persistence.*;

import java.time.Instant;
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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Image> images;

    @ManyToMany
    @JoinTable(
            name = "linked_categories",
            joinColumns = @JoinColumn(name = "event_category_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<EventCategory> categories;

    public Event() {
    }

    public Event(String createdBy,
                 Instant createdDate,
                 String lastModifiedBy,
                 Instant lastModifiedDate,
                 Long id,
                 String title,
                 String description,
                 String location,
                 String weatherConditionAvailability,
                 Set<Image> images,
                 Set<EventCategory> categories) {
        super(createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.weatherConditionAvailability = weatherConditionAvailability;
        this.images = images;
        this.categories = categories;
    }

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
