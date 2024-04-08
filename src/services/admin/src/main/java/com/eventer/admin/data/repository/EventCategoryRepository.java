package com.eventer.admin.data.repository;

import com.eventer.admin.data.model.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    boolean existsByNameIgnoreCase(String name);

    Page<EventCategory> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String term1, String term2, Pageable pageable);
}
