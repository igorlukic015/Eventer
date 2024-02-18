package com.eventer.admin.service.domain;

import com.eventer.admin.utils.Result;

public class Image {
    private Long id;
    private final String url;

    private Image(Long id, String name) {
        this.id = id;
        this.url = name;
    }

    public static Result<Image> create(Long id, String name) {
        return Result.success(new Image(id, name));
    }

    public static Result<Image> create(String name) {
        return Result.success(new Image(null, name));
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
}
