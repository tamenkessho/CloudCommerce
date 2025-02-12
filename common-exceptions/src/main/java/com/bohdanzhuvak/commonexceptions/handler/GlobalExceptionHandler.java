package com.bohdanzhuvak.commonexceptions.handler;

import com.bohdanzhuvak.commonexceptions.exception.OrderNotFoundException;
import com.bohdanzhuvak.commonexceptions.exception.ProductNotFoundException;
import com.bohdanzhuvak.commonexceptions.model.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler({OrderNotFoundException.class, ProductNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleBusinessExceptions(Exception ex) {
    log.warn("Business error: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FeignException.ServiceUnavailable.class)
  public ResponseEntity<ErrorResponse> handleFeignServiceUnavailable(FeignException ex) {
    return new ResponseEntity<>(new ErrorResponse("Dependent service is unavailable"), HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

    log.warn("Validation error: {}", errors);

    return new ResponseEntity<>(
            new ErrorResponse("Validation failed", errors),
            HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
    log.warn("Authorization denied: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorResponse("Access Denied"), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthorizationDeniedException ex) {
    log.warn("Access denied: {}", ex.getMessage());
    return new ResponseEntity<>(new ErrorResponse("Access Denied"), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("Internal server error: ", ex);
    return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
