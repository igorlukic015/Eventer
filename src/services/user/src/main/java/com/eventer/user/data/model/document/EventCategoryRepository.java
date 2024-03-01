package com.eventer.user.data.model.document;

import com.redis.om.spring.repository.RedisDocumentRepository;

public interface EventCategoryRepository extends RedisDocumentRepository<EventCategory, String> {}
