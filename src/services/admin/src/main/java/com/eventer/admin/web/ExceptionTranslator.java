package com.eventer.admin.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ExceptionTranslator {
    @ExceptionHandler(Exception.class)
    public ProblemDetail onException(Exception ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        problem.setType(URI.create("https://datatracker.ietf.org/doc/html/rfc7231#section-6.6.1"));

        return problem;
    }
}
