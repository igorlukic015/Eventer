package com.eventer.admin.domain;

import com.eventer.admin.utils.Result;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Event {
  private Long id;

  private String title;

  private String description;

  private String location;

  private Set<WeatherCondition> weatherConditionAvailability;

  private Set<EventCategory> categories;

  private Event(
      Long id,
      String title,
      String description,
      String location,
      Set<WeatherCondition> weatherConditionAvailability,
      Set<EventCategory> categories) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.location = location;
    this.weatherConditionAvailability = weatherConditionAvailability;
    this.categories = categories;
  }

  public static Result<Event> create(
      Long id,
      String title,
      String description,
      String location,
      String weatherConditionAvailability,
      Set<EventCategory> categories) {
    HashSet<WeatherCondition> weatherConditions = new HashSet<>();

    String[] weatherConditionArray = weatherConditionAvailability.split(";");

    for (String weatherCondition : weatherConditionArray) {
      Result<WeatherCondition> weatherConditionOrError = WeatherCondition.create(weatherCondition);

      if (weatherConditionOrError.isFailure()) {
        return Result.fromError(weatherConditionOrError);
      }

      weatherConditions.add(weatherConditionOrError.getValue());
    }

    Event event = new Event(id, title, description, location, weatherConditions, categories);

    return Result.success(event);
  }

  public static Result<Event> create(
      String title,
      String description,
      String location,
      Set<String> weatherConditions,
      Set<EventCategory> categories) {
    Set<Result<WeatherCondition>> weatherConditionsOrError =
        weatherConditions.stream().map(WeatherCondition::create).collect(Collectors.toSet());

    if (weatherConditionsOrError.stream().anyMatch(Result::isFailure)) {
      return Result.fromError(
          weatherConditionsOrError.stream().filter(Result::isFailure).findFirst().get());
    }

    Event event =
        new Event(
            0L,
            title,
            description,
            location,
            weatherConditionsOrError.stream().map(Result::getValue).collect(Collectors.toSet()),
            categories);

    return Result.success(event);
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }

  public Set<WeatherCondition> getWeatherConditionAvailability() {
    return weatherConditionAvailability;
  }

  public Set<EventCategory> getCategories() {
    return categories;
  }
}
