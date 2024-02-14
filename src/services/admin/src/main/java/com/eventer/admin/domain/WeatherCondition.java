package com.eventer.admin.domain;

import com.eventer.admin.utils.Result;
import com.eventer.admin.utils.ResultErrorMessages;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class WeatherCondition {
    public static WeatherCondition CLEAR = new WeatherCondition(1, "CLEAR");
    public static WeatherCondition RAIN = new WeatherCondition(2, "RAIN");
    public static WeatherCondition DRIZZLE = new WeatherCondition(3, "DRIZZLE");
    public static WeatherCondition SNOW = new WeatherCondition(4, "SNOW");
    public static WeatherCondition ATMOSPHERIC_DISTURBANCE =
            new WeatherCondition(5, "ATMOSPHERIC_DISTURBANCE");

    private static final Set<WeatherCondition> allConditions =
            Set.of(CLEAR, RAIN, DRIZZLE, SNOW, ATMOSPHERIC_DISTURBANCE);

    private final int id;

    private final String name;

    private WeatherCondition(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Result<WeatherCondition> create(String name) {
        Optional<WeatherCondition> foundCondition =
                allConditions.stream().filter(condition -> Objects.equals(condition.getName(), name)).findFirst();

        return foundCondition.map(Result::success)
                .orElseGet(() -> Result.invalid(ResultErrorMessages.invalidWeatherCondition));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
