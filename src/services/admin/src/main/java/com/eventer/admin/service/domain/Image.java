package com.eventer.admin.service.domain;

import com.eventer.admin.utils.Result;

public class Image {
    private Long id;
    private String name;

    private Image(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Result<Image> create(Long id, String name) {
        return Result.success(new Image(id, name));
    }

    public static Result<Image> create(String name) {
        return Result.success(new Image(0L, name));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
