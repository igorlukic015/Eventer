package com.eventer.user.web.dto.user;

import com.eventer.user.web.dto.image.ImageDTO;

public record UserDTO(Long id, String username, String name, String city, ImageDTO profileImage) {}
