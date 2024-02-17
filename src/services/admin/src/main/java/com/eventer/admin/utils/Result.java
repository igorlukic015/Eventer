package com.eventer.admin.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Result<T> {

    private final ResultType type;

    private final boolean isFailure;

    private final String message;

    private final T value;

    private Result(ResultType type, boolean isFailure, String message) {
        this.type = type;
        this.isFailure = isFailure;
        this.message = message;
        this.value = null;
    }

    private Result(ResultType type, boolean isFailure, String message, T value) {
        this.type = type;
        this.isFailure = isFailure;
        this.message = message;
        this.value = value;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(ResultType.SUCCESS, false, null, value);
    }

    public static Result success() {
        return new Result(ResultType.SUCCESS, false, null);
    }

    public static <T> Result<T> invalid(String message) {
        return new Result<>(ResultType.INVALID, true, message);
    }

    public static <T> Result<T> notFound(String message) {
        return new Result<>(ResultType.NOT_FOUND, true, message);
    }

    public static <T> Result<T> conflict(String message) {
        return new Result<>(ResultType.CONFLICT, true, message);
    }

    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(ResultType.UNAUTHORIZED, true, message);
    }

    public static <T> Result<T> forbidden(String message) {
        return new Result<>(ResultType.FORBIDDEN, true, message);
    }

    public static <T> Result<T> fromError(Result error) {
        return new Result<T>(error.getType(), true, error.getMessage());
    }

    public static <T> Result<Set<T>> getResultValueSet(Set<Result<T>> collectionOfResults) {
        Optional<Result<T>> firstFailureOrNull =
                collectionOfResults.stream().filter(Result::isFailure).findFirst();

        return firstFailureOrNull
                .<Result<Set<T>>>map(Result::fromError)
                .orElseGet(
                        () ->
                                Result.success(
                                        collectionOfResults.stream()
                                                .map(Result::getValue)
                                                .collect(Collectors.toSet())));
    }

    public static <T, C extends Collection<T>> Result<C> getResultValueSet(
            Collection<Result<T>> collectionOfResults, Collector<T, ?, C> collector) {
        Optional<Result<T>> firstFailureOrNull =
                collectionOfResults.stream().filter(Result::isFailure).findFirst();

        return firstFailureOrNull
                .<Result<C>>map(Result::fromError)
                .orElseGet(
                        () ->
                                Result.success(
                                        collectionOfResults.stream()
                                                .map(Result::getValue)
                                                .collect(collector)));
    }

    public ResultType getType() {
        return type;
    }

    public boolean isFailure() {
        return isFailure;
    }

    public boolean isSuccess() {
        return !isFailure;
    }

    public String getMessage() {
        return message;
    }

    public T getValue() {
        return value;
    }
}
