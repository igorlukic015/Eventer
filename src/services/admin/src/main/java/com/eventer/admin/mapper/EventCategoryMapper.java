package com.eventer.admin.mapper;

import com.eventer.admin.contracts.eventcategory.CreateEventCategoryRequest;
import com.eventer.admin.web.dto.eventcategory.EventCategoryDTO;
import com.eventer.admin.utils.Result;
import com.eventer.admin.domain.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class EventCategoryMapper {

    public static EventCategoryDTO toDTO(EventCategory domain) {
        return new EventCategoryDTO(domain.getId(), domain.getName(), domain.getDescription());
    }

    public static Page<EventCategoryDTO> toDTOs(Page<EventCategory> categories) {
        List<EventCategoryDTO> dtos = categories.stream().map(EventCategoryMapper::toDTO).toList();

        return new PageImpl<>(dtos, categories.getPageable(), categories.getTotalElements());
    }

    public static Result<EventCategory> toDomain(com.eventer.admin.model.EventCategory model) {
        Result<EventCategory> eventCategoryOrError =
                EventCategory.create(model.getId(), model.getName(), model.getDescription());

        if (eventCategoryOrError.isFailure()) {
            return Result.fromError(eventCategoryOrError);
        }

        return eventCategoryOrError;
    }

    public static Result<Page<EventCategory>> toDomainPage(
            Page<com.eventer.admin.model.EventCategory> foundCategories) {
        List<Result<EventCategory>> categories =
                foundCategories.stream().map(EventCategoryMapper::toDomain).toList();

        if (categories.stream().anyMatch(Result::isFailure)) {
            return Result.fromError(
                    categories.stream().filter(Result::isFailure).findFirst().get());
        }

        Page<EventCategory> result =
                new PageImpl<>(
                        categories.stream().map(Result::getValue).toList(),
                        foundCategories.getPageable(),
                        foundCategories.getTotalElements());

        return Result.success(result);
    }

    public static com.eventer.admin.model.EventCategory toModel(
            EventCategory eventCategory, String createdBy) {
        var model = new com.eventer.admin.model.EventCategory();

        model.setId(eventCategory.getId());
        model.setName(eventCategory.getName());
        model.setDescription(eventCategory.getDescription());
        model.setCreatedBy(createdBy);

        return model;
    }

    public static CreateEventCategoryRequest toRequest(EventCategoryDTO dto) {
        return new CreateEventCategoryRequest(dto.name(), dto.description());
    }
}
