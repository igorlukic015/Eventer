package com.eventer.user.contracts.comment;

public record CreateCommentRequest(String text, Long eventId) {}
