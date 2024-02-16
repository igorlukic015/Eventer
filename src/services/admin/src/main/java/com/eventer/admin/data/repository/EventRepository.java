package com.eventer.admin.data.repository;

import com.eventer.admin.data.model.Event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
