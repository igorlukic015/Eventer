package com.eventer.user.cache.data.model;

import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import com.redis.om.spring.annotations.Document;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Document
public class EventCategory {
    @Id private String uid;

    @Indexed @Searchable private Long id;

    @Indexed @Searchable private String name;

    private String description;

    public EventCategory() {}

    public EventCategory(Long id, String name, String description) {
        this.uid = null;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventCategory that = (EventCategory) o;
        return Objects.equals(uid, that.uid)
                || Objects.equals(id, that.id)
                || Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, id, name);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
