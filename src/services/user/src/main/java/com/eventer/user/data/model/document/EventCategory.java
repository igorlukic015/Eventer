package com.eventer.user.data.model.document;

import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import com.redis.om.spring.annotations.Document;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Document
public class EventCategory {
    @Id public String uid;

    @Indexed @Searchable public Long id;

    @Indexed @Searchable public String name;

    public String description;

    public EventCategory() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventCategory that = (EventCategory) o;
        return Objects.equals(uid, that.uid) || Objects.equals(id, that.id) || Objects.equals(name, that.name);
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
