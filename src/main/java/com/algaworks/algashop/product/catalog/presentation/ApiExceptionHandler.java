package com.algaworks.algashop.product.catalog.presentation;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                            final HttpHeaders headers,
                                                                            final HttpStatusCode status,
                                                                            final WebRequest request) {
        final var problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Invalid fields");
        problemDetail.setDetail("One or more fields are invalid");
        problemDetail.setType(URI.create("/errors/invalid-fields"));
        final var fieldErrors = ex.getBindingResult().getAllErrors().stream().collect(Collectors.toMap(
                e -> ((FieldError) e).getField(),
                e -> messageSource.getMessage(e, LocaleContextHolder.getLocale())
        ));
        problemDetail.setProperty("fields", fieldErrors);
        log.error(ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleDomainException(final Exception ex,
                                               final WebRequest request) {
        final var problemDetail = ProblemDetail.forStatus(INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("An unexpected internal error occurred");
        problemDetail.setType(URI.create("/errors/internal-server-error"));
        log.error(ex.getMessage(), ex);
        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(final ResourceNotFoundException ex,
                                                         final WebRequest request) {
        final var problemDetail = ProblemDetail.forStatus(NOT_FOUND);
        problemDetail.setTitle("Not Found");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("/errors/not-found"));
        log.error(ex.getMessage(), ex);
        return problemDetail;
    }

}
