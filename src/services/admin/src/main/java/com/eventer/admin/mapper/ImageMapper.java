package com.eventer.admin.mapper;

import com.eventer.admin.config.ApplicationConfiguration;
import com.eventer.admin.service.domain.Event;
import com.eventer.admin.service.domain.Image;
import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.web.dto.event.ImageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ImageMapper {
    public static ImageDTO toDTO(Image domain) {
        return new ImageDTO(domain.getId(), domain.getUrl());
    }

    public static Result<Image> toDomain(ImageDTO dto) {
        return Image.create(dto.id(), dto.url());
    }

    public static Result<Set<Image>> toDomainSet(Set<ImageDTO> dtos) {
        return Result.getResultValueSet(
                dtos.stream().map(ImageMapper::toDomain).collect(Collectors.toSet()));
    }

    public static Result<Image> toDomain(com.eventer.admin.data.model.Image model) {
        String url =
                String.format(
                        "%s/%s/%s/%s",
                        ApplicationConfiguration.getImageBaseUrl(),
                        ApplicationConfiguration.getImageBucketName(),
                        Event.class.getSimpleName(),
                        model.getName());

        return Image.create(model.getId(), url);
    }

    public static Result<Set<Image>> toDomainSet(List<com.eventer.admin.data.model.Image> models) {
        return Result.getResultValueSet(
                models.stream().map(ImageMapper::toDomain).collect(Collectors.toSet()));
    }

    public static com.eventer.admin.data.model.Image toModel(Image domain) {
        String name = Arrays.stream(domain.getUrl().split("/")).toList().getLast();

        com.eventer.admin.data.model.Image model = new com.eventer.admin.data.model.Image();
        model.setId(domain.getId());
        model.setName(name);

        return model;
    }
}
