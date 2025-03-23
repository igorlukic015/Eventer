package com.eventer.admin.service.domain;

import com.eventer.admin.config.ApplicationConfiguration;
import com.github.igorlukic015.resulter.Result;

public class Image {
    private Long id;
    private final String url;
    private final String name;

    private Image(Long id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public static Result<Image> create(Long id, String name) {
        String url =
                String.format(
                        "%s/%s/%s",
                        ApplicationConfiguration.getImageBaseUrl(),
                        ApplicationConfiguration.getImageBucketName(),
                        name);

        return Result.success(new Image(id, url, name));
    }

    public static Result<Image> create(String name) {
        String url =
                String.format(
                        "%s/%s/%s",
                        ApplicationConfiguration.getImageBaseUrl(),
                        ApplicationConfiguration.getImageBucketName(),
                        name);

        return Result.success(new Image(null, url, name));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
