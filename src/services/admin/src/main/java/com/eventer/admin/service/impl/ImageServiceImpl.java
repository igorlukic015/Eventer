package com.eventer.admin.service.impl;

import com.eventer.admin.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${image.base.url}")
    private String baseUrl;

    public Set<String> saveImages(Set<MultipartFile> images) {
        return new HashSet<String>();
    }
}
