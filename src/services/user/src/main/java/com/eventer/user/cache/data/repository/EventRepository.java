package com.eventer.user.cache.data.repository;

import com.eventer.user.cache.data.model.Event;
import com.redis.om.spring.repository.RedisDocumentRepository;

public interface EventRepository extends RedisDocumentRepository<Event, String> {}
