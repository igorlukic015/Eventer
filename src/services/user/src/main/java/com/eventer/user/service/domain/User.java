package com.eventer.user.service.domain;

import com.github.cigor99.resulter.Result;
import org.springframework.util.StringUtils;

public class User {
    private final Long id;
    private final String username;
    private final String password;
    private final Image profileImage;

    private User(Long id, String username, String password, Image profileImage) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
    }

    public static Result<User> create(Long id, String username, String password, Result<Image> imageOrError) {
        if (imageOrError.isFailure()) {
            return Result.fromError(imageOrError);
        }

        User user = new User(id, username, password, imageOrError.getValue());

        return Result.success(user);
    }

    public static Result<User> create(String username, String password, Image image) {
        if (!StringUtils.hasText(username) || username.length() > 255) {
            return Result.invalid("INVALID_USER_USERNAME");
        }

        if (!StringUtils.hasText(password) || password.length() < 6 || password.length() > 255) {
            return Result.invalid("INVALID_USER_PASSWORD");
        }

        User user = new User(null, username, password, image);

        return Result.success(user);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Image getProfileImage() {
        return profileImage;
    }
}
