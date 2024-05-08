package com.eventer.user.mapper;

import com.eventer.user.service.domain.Image;
import com.eventer.user.web.dto.image.ImageDTO;
import com.github.igorlukic015.resulter.Result;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ImageMapper {
    public static ImageDTO toDTO(Image image) {
        if (image == null) {
            return null;
        }

        return new ImageDTO(image.getId(), image.getUrl(), image.getName());
    }

    public static Result<Image> toDomain(ImageDTO dto) {
        if (dto == null) {
            return null;
        }

        return Image.create(dto.id(), dto.name());
    }

    public static Result<Set<Image>> toDomainSet(Set<ImageDTO> dtos) {
        return Result.getResultValueSet(
                dtos.stream().map(ImageMapper::toDomain).collect(Collectors.toSet()));
    }

    public static Result<Image> toDomain(com.eventer.user.data.model.Image model) {
        if (model == null) {
            return null;
        }

        return Image.create(model.getId(), model.getName());
    }

    public static Result<Set<Image>> toDomainSet(List<com.eventer.user.data.model.Image> models) {
        return Result.getResultValueSet(
                models.stream().map(ImageMapper::toDomain).collect(Collectors.toSet()));
    }

    public static com.eventer.user.data.model.Image toModel(Image domain) {
        if (domain == null) {
            return null;
        }

        com.eventer.user.data.model.Image model = new com.eventer.user.data.model.Image();
        model.setId(domain.getId());
        model.setName(domain.getName());

        return model;
    }
}
