package com.bohdanzhuvak.commonexceptions.handler;

import com.bohdanzhuvak.commonexceptions.exception.BaseException;
import com.bohdanzhuvak.commonexceptions.exception.impl.InvalidCredentialsException;
import com.bohdanzhuvak.commonexceptions.exception.impl.InvalidOrderStateException;
import com.bohdanzhuvak.commonexceptions.exception.impl.InvalidTokenException;
import com.bohdanzhuvak.commonexceptions.exception.impl.ResourceNotFoundException;
import com.bohdanzhuvak.commonexceptions.exception.impl.ServiceUnavailableException;
import com.bohdanzhuvak.commonexceptions.exception.impl.UserAlreadyExistsException;
import com.bohdanzhuvak.commonexceptions.model.ApiError;
import com.bohdanzhuvak.commonexceptions.model.ValidationDetail;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ApiError> handleBaseException(BaseException ex, WebRequest request) {
    ApiError error = buildApiError(ex, request);
    log.warn("Business error: {}", ex.getMessage());
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(FeignException.ServiceUnavailable.class)
  public ResponseEntity<ApiError> handleFeignServiceUnavailable(FeignException ex, WebRequest request) {
    ServiceUnavailableException customEx = new ServiceUnavailableException(extractServiceName(ex));
    ApiError error = buildApiError(customEx, request);
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
    ApiError error = buildApiError(ex, request);
    log.warn("Access denied: {}", request.getDescription(false));
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler({InvalidTokenException.class, AuthorizationDeniedException.class, InvalidCredentialsException.class})
  public ResponseEntity<ApiError> handleAuthErrors(BaseException ex, WebRequest request) {
    ApiError error = buildApiError(ex, request);
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleUserExists(UserAlreadyExistsException ex, WebRequest request) {
    ApiError error = buildApiError(ex, request);
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(
      MethodArgumentNotValidException ex,
      WebRequest request
  ) {
    ApiError error = buildApiError(ex, request);

    ApiError customizedError = new ApiError(
        error.timestamp(),
        error.status(),
        error.error(),
        "Validation failed",
        error.path(),
        "VALIDATION_FAILED",
        error.details()
    );

    log.warn("Validation error: {}", customizedError.details());
    return ResponseEntity.badRequest().body(customizedError);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleAllExceptions(Exception ex, WebRequest request) {
    ApiError error = buildApiError(ex, request);
    log.error("Internal error: {}", ex.getMessage(), ex);
    return ResponseEntity.internalServerError().body(error);
  }

  private ApiError buildApiError(Exception ex, WebRequest request) {
    HttpStatus status = resolveHttpStatus(ex);
    String path = ((ServletWebRequest) request).getRequest().getRequestURI();
    return new ApiError(
        Instant.now().toString(),
        status.value(),
        status.getReasonPhrase(),
        ex.getMessage(),
        path,
        (ex instanceof BaseException) ? ((BaseException) ex).getErrorCode() : null,
        resolveDetails(ex)
    );
  }

  private List<ValidationDetail> resolveDetails(Exception ex) {
    if (ex instanceof MethodArgumentNotValidException) {
      return ((MethodArgumentNotValidException) ex).getBindingResult()
          .getFieldErrors()
          .stream()
          .map(error -> new ValidationDetail(error.getField(), error.getDefaultMessage()))
          .toList();
    }
    return null;
  }

  private String extractServiceName(FeignException ex) {
    String url = ex.request().url();

    if (url.contains("/api/users")) {
      return "user-service";
    } else if (url.contains("/api/products")) {
      return "product-service";
    } else if (url.contains("/api/orders")) {
      return "order-service";
    }
    return "unknown-service";
  }

  private HttpStatus resolveHttpStatus(Exception ex) {
    if (ex instanceof ResourceNotFoundException) return HttpStatus.NOT_FOUND;
    if (ex instanceof AccessDeniedException) return HttpStatus.FORBIDDEN;
    if (ex instanceof InvalidTokenException) return HttpStatus.UNAUTHORIZED;
    if (ex instanceof InvalidCredentialsException) return HttpStatus.UNAUTHORIZED;
    if (ex instanceof UserAlreadyExistsException) return HttpStatus.CONFLICT;
    if (ex instanceof ServiceUnavailableException) return HttpStatus.SERVICE_UNAVAILABLE;
    if (ex instanceof MethodArgumentNotValidException) return HttpStatus.BAD_REQUEST;
    if (ex instanceof InvalidOrderStateException) return HttpStatus.CONFLICT;
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
