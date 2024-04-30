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

    @Value("${image.host.url}")
    public void setImageBaseUrl(String imageHostUrl) {
        ApplicationConfiguration.imageBaseUrl = imageHostUrl;
    }

    private static String imageBucketName;

    public static String getImageBucketName() {
        return imageBucketName;
    }

    @Value("${s3.bucket.name}")
    public void setImageBucketName(String imageBucketName) {
        ApplicationConfiguration.imageBucketName = imageBucketName;
    }
}
