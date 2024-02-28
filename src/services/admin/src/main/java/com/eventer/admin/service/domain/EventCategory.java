package com.eventer.admin.service.domain;

import com.eventer.admin.utils.Helpers;
import com.github.cigor99.resulter.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.util.StringUtils;

public class EventCategory {

  private final Long id;

  private final String name;

  private final String description;

  private EventCategory(Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public static Result<EventCategory> create(Long id, String name, String description) {
    EventCategory eventCategory = new EventCategory(id, name, description);

    return Result.success(eventCategory);
  }

  public static Result<EventCategory> create(String name, String description) {
    if (StringUtils.hasText(name) || name.length() > 255) {
      return Result.invalid(ResultErrorMessages.invalidEventCategoryName);
    }
    
    if (description.length() > 255) {
        return Result.invalid(ResultErrorMessages.invalidEventCategoryDescription);
    }
    
    EventCategory eventCategory = new EventCategory(null, name, description);

    return Result.success(eventCategory);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
