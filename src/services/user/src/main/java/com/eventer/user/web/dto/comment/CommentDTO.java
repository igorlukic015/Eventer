package com.eventer.user.web.dto.comment;

public record CommentDTO(Long id, String text, Long eventId, Long userId, String userUsername, String userProfileImageUrl, Boolean isEdited) {}
