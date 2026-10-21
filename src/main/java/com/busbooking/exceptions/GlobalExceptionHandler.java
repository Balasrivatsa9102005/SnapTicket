
package com.busbooking.exceptions;

import java.time.Instant;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.busbooking.dto.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleBookingConflict(
            BookingConflictException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.FORBIDDEN,
                "You are not allowed to access this resource",
                request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildResponse(
                HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or contains invalid JSON",
                request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "The request conflicts with existing data",
                request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(
            Exception ex,
            HttpServletRequest request) {

        
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request);
    }
    
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(
            org.springframework.security.authentication.BadCredentialsException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED, 
                "Invalid email or password",
                request
        );
    }
    
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchElement(
            java.util.NoSuchElementException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST, 
                "Requested resource or seat element could not be found in the database.",
                request
        );
    }
    
    @ExceptionHandler(com.busbooking.exceptions.InvalidSeatException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSeat(
            com.busbooking.exceptions.InvalidSeatException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(),
                request
        );
    }



}
