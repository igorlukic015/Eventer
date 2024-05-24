package com.eventer.admin.data.repository;

import com.eventer.admin.data.model.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT 1 FROM Event e JOIN e.categories c WHERE c.id = :categoryId")
    Optional<Integer> checkIfCategoryIsConnectedToEvent(@Param("categoryId") Long categoryId);

    Page<Event> findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String term1, String term2, Pageable pageable);
}
