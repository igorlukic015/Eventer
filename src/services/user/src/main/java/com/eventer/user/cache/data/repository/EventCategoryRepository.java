package com.eventer.user.cache.data.repository;

import com.eventer.user.cache.data.model.EventCategory;
import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.Optional;

public interface EventCategoryRepository extends RedisDocumentRepository<EventCategory, String> {
    Optional<EventCategory> findOneByCategoryId(Long categoryId);
    Optional<EventCategory> findOneByName(String name);
}
