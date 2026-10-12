package com.medtriage.exception;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception, WebRequest request) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "Solicitud inválida", exception.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException exception, WebRequest request) {
        return createProblemDetail(HttpStatus.CONFLICT, "Operación no permitida", exception.getMessage(), request);
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create("https://medtriage.local/problems/" + status.value()));
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getDescription(false).replaceFirst("^uri=", "")));
        return problemDetail;
    }
}
