package com.mercadoaurora.order.api;
import com.mercadoaurora.order.api.dto.ApiErrorResponse;
import com.mercadoaurora.order.application.exception.OrderConflictException;
import com.mercadoaurora.order.application.exception.OrderIntegrationException;
import com.mercadoaurora.order.application.exception.OrderNotFoundException;
import com.mercadoaurora.order.application.exception.PaymentProcessingException;
import com.mercadoaurora.order.domain.DomainValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.Instant;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(OrderNotFoundException exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", exception.getMessage(), request);
    }
    @ExceptionHandler(OrderConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(OrderConflictException exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "ORDER_CONFLICT", exception.getMessage(), request);
    }
    @ExceptionHandler(OrderIntegrationException.class)
    public ResponseEntity<ApiErrorResponse> handleIntegration(OrderIntegrationException exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, "ORDER_INTEGRATION_ERROR", exception.getMessage(), request);
    }
    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handlePayment(PaymentProcessingException exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "PAYMENT_UNAVAILABLE", exception.getMessage(), request);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJson(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Malformed JSON request", request);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Invalid path parameter: " + exception.getName(),
                request
        );
    }
    @ExceptionHandler({
            DomainValidationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiErrorResponse> handleValidation(RuntimeException exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", exception.getMessage(), request);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining("; "));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", message, request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Unexpected error", request);
    }
    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        ));
    }
}
