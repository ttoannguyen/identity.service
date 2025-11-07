package com.ttoannguyen.identity_service.exception;

import com.ttoannguyen.identity_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  ResponseEntity<ApiResponse<?>> handlingRuntimeException(Exception exception) {

    exception.printStackTrace(); // Log để dễ debug

    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
  }

  @ExceptionHandler(value = AppException.class)
  ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
    ErrorCode errorCode = exception.getErrorCode();

    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<ApiResponse<?>> handlingAccessDeniedException(AccessDeniedException exception) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getHttpStatusCode())
        .body(
            ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
    var fieldError = exception.getFieldError();

    String enumKey = (fieldError != null) ? fieldError.getDefaultMessage() : null;

    ErrorCode errorCode;
    try {
      errorCode = (enumKey != null) ? ErrorCode.valueOf(enumKey) : ErrorCode.INVALID_KEY;
    } catch (IllegalArgumentException e) {
      errorCode = ErrorCode.INVALID_KEY;
    }
    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setCode(errorCode.getCode());
    apiResponse.setMessage(errorCode.getMessage());

    return ResponseEntity.badRequest().body(apiResponse);
  }
}
