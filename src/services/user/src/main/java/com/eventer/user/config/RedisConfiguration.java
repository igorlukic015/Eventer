package com.eventer.user.config;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRedisDocumentRepositories(
        basePackages = "com.eventer.user.cache.data")
public class RedisConfiguration {}
