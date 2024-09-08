package com.eventer.user.service.domain;

import com.github.igorlukic015.resulter.Result;
import org.springframework.util.StringUtils;

public class User {
    private final Long id;
    private final String name;
    private final String username;
    private final String password;
    private final String city;
    private final Image profileImage;

    private User(
            Long id,
            String name,
            String username,
            String password,
            String city,
            Image profileImage) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.city = city;
        this.profileImage = profileImage;
    }

    public static Result<User> create(
            Long id,
            String name,
            String username,
            String password,
            String city,
            Result<Image> imageOrError) {
        User user =
                new User(
                        id,
                        name,
                        username,
                        password,
                        city,
                        imageOrError != null ? imageOrError.getValue() : null);

        return Result.success(user);
    }

    public static Result<User> create(String name, String username, String password, String city) {
        if (!StringUtils.hasText(name) || name.length() > 255) {
            return Result.invalid("INVALID_NAME");
        }

        if (!StringUtils.hasText(city) || city.length() > 255) {
            return Result.invalid("INVALID_CITY");
        }

        if (!StringUtils.hasText(username) || username.length() > 255) {
            return Result.invalid("INVALID_USER_USERNAME");
        }

        if (!StringUtils.hasText(password) || password.length() > 255) {
            return Result.invalid("INVALID_USER_PASSWORD");
        }

        User user = new User(null, name, username, password, city, null);

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

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Image getProfileImage() {
        return profileImage;
    }
}
