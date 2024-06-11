package com.eventer.user.service.domain;

import com.github.igorlukic015.resulter.Result;

public class Comment {
    private Long id;
    private String text;
    private Long eventId;
    private Long userId;
    private String userProfileImageUrl;

    private Comment(Long id, String text, Long eventId, Long userId, String userProfileImageUrl) {
        this.id = id;
        this.text = text;
        this.eventId = eventId;
        this.userId = userId;
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public static Result<Comment> create(Long id, String text, Long eventId, Long userId, String userProfileImageUrl) {
        return Result.success(new Comment(id, text, eventId, userId, userProfileImageUrl));
    }

    public static Result<Comment> create(String text, Long eventId, Long userId, String userProfileImageUrl) {
        return Result.success(new Comment(null, text, eventId, userId, userProfileImageUrl));
    }

    public static Result<Comment> partialCreate(String text, Long eventId) {
        return Result.success(new Comment(null, text, eventId, null, null));
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }
}
