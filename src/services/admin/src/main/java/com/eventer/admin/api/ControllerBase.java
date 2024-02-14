package com.eventer.admin.api;

import com.eventer.admin.utils.Result;
import com.eventer.admin.utils.ResultType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

public abstract class ControllerBase {

    protected final <T, R> ResponseEntity<?> okOrError(Result<T> result, Function<T, R> mapper) {
        if (result.isSuccess()) {
            return ResponseEntity.ok(mapper.apply(result.getValue()));
        }

        return this.getErrorResponse(result);
    }

    private <T> ResponseEntity<ProblemDetail> getErrorResponse(Result<T> result) {
        HttpStatus status = getHttpStatus(result.getType());

        return ResponseEntity.status(status)
                .body(ProblemDetail.forStatusAndDetail(status, result.getMessage()));
    }

    private HttpStatus getHttpStatus(ResultType resultType) {
        return switch (resultType) {
            case INVALID -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case SUCCESS -> HttpStatus.OK;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
