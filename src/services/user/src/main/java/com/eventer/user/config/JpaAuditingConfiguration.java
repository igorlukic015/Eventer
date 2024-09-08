package com.eventer.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {

    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final String DEFAULT_USER = "SYSTEM";

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                    && authentication.isAuthenticated()
                    && !(authentication.getPrincipal() instanceof String
                    && ANONYMOUS_USER.equals(authentication.getPrincipal()))) {
                return Optional.ofNullable(authentication.getName());
            }
            return Optional.of(DEFAULT_USER);
        };
    }
}
