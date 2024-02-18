package com.eventer.admin.service.impl;

import com.eventer.admin.config.ApplicationConfiguration;
import com.eventer.admin.data.repository.ImageRepository;
import com.eventer.admin.mapper.ImageMapper;
import com.eventer.admin.service.ImageService;
import com.eventer.admin.service.domain.Image;
import com.eventer.admin.utils.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Result<Set<Image>> getImagesByIds(Set<Long> ids) {
        List<com.eventer.admin.data.model.Image> foundImages =
                this.imageRepository.findAllById(ids);

        if (foundImages.size() == 0) {
            return Result.notFound(ResultErrorMessages.imagesNotFound);
        }

        return ImageMapper.toDomainSet(foundImages);
    }

    @Override
    public Set<Path> saveImageFiles(List<MultipartFile> images, String entityName) {
        Set<Path> savedImagePaths = new HashSet<>();

        Path folder =
                Paths.get(
                        String.format(
                                "%s/%s", ApplicationConfiguration.getImageBaseUrl(), entityName));

        for (MultipartFile image : images) {
            String[] fileNameTokens =
                    StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()))
                            .split("\\.");

            String extension =
                    fileNameTokens.length > 0 ? fileNameTokens[fileNameTokens.length - 1] : ".jpg";

            String name =
                    String.format(
                            "%s_%s.%s",
                            UUID.randomUUID(), Instant.now().getEpochSecond(), extension);

            Path fullPath = Path.of(folder.toString(), name);

            try {
                if (!Files.exists(folder)) {
                    Files.createDirectories(folder);
                }

                Files.copy(image.getInputStream(), fullPath);
            } catch (IOException e) {
                this.deleteOnFailure(savedImagePaths);
                return new HashSet<>();
            }

            savedImagePaths.add(fullPath);
        }

        return savedImagePaths;
    }

    @Override
    public Result<Image> saveImage(Image imageToSave, String entityName) {
        com.eventer.admin.data.model.Image savedImage;
        try {
            savedImage = this.imageRepository.save(ImageMapper.toModel(imageToSave, entityName));
        } catch (Exception e) {
            return Result.invalid(e.getMessage());
        }

        return ImageMapper.toDomain(savedImage);
    }

    @Override
    public void deleteOnFailure(Set<Path> savedImagePaths) {
        for (Path path : savedImagePaths) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // TODO: Add logging
            }
        }
    }
}
