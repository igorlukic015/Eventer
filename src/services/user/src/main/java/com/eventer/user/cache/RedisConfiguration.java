package com.eventer.user.cache;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRedisDocumentRepositories(
        basePackages = "com.eventer.user.data.model.document")
public class RedisConfiguration {}
