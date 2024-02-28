package com.eventer.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Profile(value = "dev")
@Component
public class ApplicationConfiguration {
    private static String imageBaseUrl;

    public static String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public static String getImageUrlSeparator() {
        return FileSystems.getDefault().getSeparator();
    }

    @Value("${image.base.url}")
    public void setImageBaseUrl(String imageBaseUrl) {
        Path absolutePath = Path.of(imageBaseUrl).toAbsolutePath();
        ApplicationConfiguration.imageBaseUrl = absolutePath.toString().replace(File.pathSeparator, "/");
    }
}
