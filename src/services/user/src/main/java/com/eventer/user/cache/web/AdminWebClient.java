package com.eventer.user.cache.web;

import com.eventer.user.security.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class AdminWebClient {
    @Value("${admin-url}")
    private String adminUrl;

    private final JwtService jwtService;

    public AdminWebClient(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public WebClient.ResponseSpec loadEntityClient(
            String uri) {
        String token = this.jwtService.generateServiceToken();

        return WebClient.builder()
                .baseUrl(this.adminUrl)
                .build()
                .get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token))
                .retrieve();
    }
}
