package com.eventer.admin.repository;

import com.eventer.admin.model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    boolean existsByNameIgnoreCase(String name);
}
