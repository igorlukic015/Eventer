package com.eventer.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration {
    private static String imageBaseUrl;

    public static String getImageBaseUrl() {
        return imageBaseUrl;
    }

    @Value("${image.base.url}")
    public void setImageBaseUrl(String imageBaseUrl) {
        ApplicationConfiguration.imageBaseUrl = imageBaseUrl;
    }
}
