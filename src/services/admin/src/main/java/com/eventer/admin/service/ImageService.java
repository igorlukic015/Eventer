package com.eventer.admin.service;

import com.eventer.admin.service.domain.Image;
import com.eventer.admin.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface ImageService {
    Result<Set<Image>> getImagesByIds(Set<Long> ids);

    Set<Path> saveImageFiles(List<MultipartFile> images, String entityName);

    void deleteOnFailure(Set<Path> savedImagePaths);

    Result<Image> saveImage(Image imageToSave, String entityName);
}
