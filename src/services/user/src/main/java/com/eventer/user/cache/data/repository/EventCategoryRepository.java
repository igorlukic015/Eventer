package com.eventer.user.cache.data.repository;

import com.eventer.user.cache.data.model.EventCategory;
import com.redis.om.spring.repository.RedisDocumentRepository;

public interface EventCategoryRepository extends RedisDocumentRepository<EventCategory, String> {}
