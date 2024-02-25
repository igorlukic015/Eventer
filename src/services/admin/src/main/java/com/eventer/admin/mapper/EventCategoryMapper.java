package com.eventer.admin.mapper;

import com.eventer.admin.contracts.eventcategory.CreateEventCategoryRequest;
import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;
import com.github.cigor99.resulter.Result;
import com.eventer.admin.service.domain.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventCategoryMapper {

    public static EventCategoryDTO toDTO(EventCategory domain) {
        return new EventCategoryDTO(domain.getId(), domain.getName(), domain.getDescription());
    }

    public static Page<EventCategoryDTO> toDTOs(Page<EventCategory> categories) {
        List<EventCategoryDTO> dtos = categories.stream().map(EventCategoryMapper::toDTO).toList();

        return new PageImpl<>(dtos, categories.getPageable(), categories.getTotalElements());
    }

    public static Result<EventCategory> toDomain(EventCategoryDTO dto) {
        return EventCategory.create(dto.id(), dto.name(), dto.description());
    }

    public static Result<Set<EventCategory>> toDomainSet(Set<EventCategoryDTO> dtos) {
        return Result.getResultValueSet(
                dtos.stream().map(EventCategoryMapper::toDomain).collect(Collectors.toSet()));
    }

    public static Result<EventCategory> toDomain(com.eventer.admin.data.model.EventCategory model) {
        Result<EventCategory> eventCategoryOrError =
                EventCategory.create(model.getId(), model.getName(), model.getDescription());

        if (eventCategoryOrError.isFailure()) {
            return Result.fromError(eventCategoryOrError);
        }

        return eventCategoryOrError;
    }

    public static Result<Set<EventCategory>> toDomainSet(
            List<com.eventer.admin.data.model.EventCategory> models) {
        return Result.getResultValueSet(
                models.stream().map(EventCategoryMapper::toDomain).collect(Collectors.toSet()));
    }

    public static Result<Page<EventCategory>> toDomainPage(
            Page<com.eventer.admin.data.model.EventCategory> foundCategories) {
        Result<List<EventCategory>> categoriesOrError =
                Result.getResultValueSet(
                        foundCategories.stream().map(EventCategoryMapper::toDomain).toList(),
                        Collectors.toList());

        if (categoriesOrError.isFailure()) {
            return Result.fromError(categoriesOrError);
        }

        Page<EventCategory> result =
                new PageImpl<>(
                        categoriesOrError.getValue(),
                        foundCategories.getPageable(),
                        foundCategories.getTotalElements());

        return Result.success(result);
    }

    public static com.eventer.admin.data.model.EventCategory toModel(EventCategory eventCategory) {
        var model = new com.eventer.admin.data.model.EventCategory();

        model.setId(eventCategory.getId());
        model.setName(eventCategory.getName());
        model.setDescription(eventCategory.getDescription());

        return model;
    }

    public static CreateEventCategoryRequest toRequest(EventCategoryDTO dto) {
        return new CreateEventCategoryRequest(dto.name(), dto.description());
    }
}
