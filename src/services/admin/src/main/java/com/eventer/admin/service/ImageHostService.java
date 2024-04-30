package com.eventer.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ImageHostService {
    Set<String> saveAllImages(List<MultipartFile> images, String entityName);

    void deleteAll(Set<String> images);

    String getImageUrl(String imageName);
}
