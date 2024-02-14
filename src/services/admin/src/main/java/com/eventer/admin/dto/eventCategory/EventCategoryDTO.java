package com.eventer.admin.dto.eventCategory;

public record EventCategoryDTO(Long id, String name, String description) {
    public EventCategoryDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
