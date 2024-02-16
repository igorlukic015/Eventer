package com.eventer.admin.contracts.eventcategory;

public record CreateEventCategoryRequest(String name, String description){
    public CreateEventCategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

