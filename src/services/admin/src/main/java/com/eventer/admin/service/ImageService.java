package com.eventer.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface ImageService {
    Set<String> saveImages(Set<MultipartFile> images);
}
