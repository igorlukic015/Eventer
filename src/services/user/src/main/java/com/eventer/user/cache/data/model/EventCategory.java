package com.eventer.user.cache.data.model;

import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import com.redis.om.spring.annotations.Document;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Document
public class EventCategory {
    @Id private String id;

    @Indexed @Searchable private Long categoryId;

    @Indexed @Searchable private String name;

    private String description;

    public EventCategory() {}

    public EventCategory(Long categoryId, String name, String description) {
        this.id = null;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventCategory that = (EventCategory) o;
        return Objects.equals(id, that.id)
                || Objects.equals(categoryId, that.categoryId)
                || Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
