package com.eventer.admin.mapper;

import com.eventer.admin.dto.EventCategoryDTO;
import com.eventer.admin.utils.Result;
import com.eventer.admin.domain.EventCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

public class EventCategoryMapper {

    public static EventCategoryDTO toDTO(EventCategory domain) {
        return new EventCategoryDTO(
                domain.getId(),
                domain.getName(),
                domain.getDescription()
        );
    }

    public static Page<EventCategoryDTO> toDTOs(Page<EventCategory> categories) {
        List<EventCategoryDTO> dtos = categories.stream().map(EventCategoryMapper::toDTO).toList();

        return new PageImpl<>(
                dtos,
                categories.getPageable(),
                categories.getTotalElements());
    }

    public static Result<EventCategory> toDomain(com.eventer.admin.model.EventCategory model) {
        return EventCategory.create(
                model.getId(),
                model.getName(),
                model.getDescription());
    }

    public static Result<Page<EventCategory>> toDomainPage(Page<com.eventer.admin.model.EventCategory> foundCategories) {
        ArrayList<EventCategory> categories = new ArrayList<>();

        for (com.eventer.admin.model.EventCategory foundCategory : foundCategories) {
            Result<EventCategory> domainOrError = toDomain(foundCategory);

            if (domainOrError.isFailure()){
                return Result.fromError(domainOrError);
            }

            categories.add(domainOrError.getValue());
        }

        Page<EventCategory> result = new PageImpl<>(
                categories,
                foundCategories.getPageable(),
                foundCategories.getTotalElements());

        return Result.success(result);
    }
}
