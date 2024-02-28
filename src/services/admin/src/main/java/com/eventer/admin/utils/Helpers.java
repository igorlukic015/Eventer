package com.eventer.admin.utils;

import com.eventer.admin.config.ApplicationConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

public class Helpers {
    public static void deleteFilesFromPathSet(Set<Path> savedFilePaths) {
        for (Path path : savedFilePaths) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // TODO: Add logging
            }
        }
    }

    public static Set<Path> saveImages(List<MultipartFile> images, String entityName) {
        Set<Path> savedImagePaths = new HashSet<>();

        Path folder =
                Paths.get(
                        String.format(
                                "%s%s%s",
                                ApplicationConfiguration.getImageBaseUrl(),
                                ApplicationConfiguration.getImageUrlSeparator(),
                                entityName));

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
                Helpers.deleteFilesFromPathSet(savedImagePaths);
                return new HashSet<>();
            }

            savedImagePaths.add(fullPath);
        }

        return savedImagePaths;
    }
}
