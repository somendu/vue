package com.vqd.tme.na2a.controller;

import com.vqd.tme.na2a.exception.UnauthorizedException;
import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.ProductNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
  @Data
  @AllArgsConstructor
  public static class ErrorResponse<T> {
    private T error;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse<String> handleProductNotFoundException(ProductNotFoundException ex) {
    return new ErrorResponse<>(ex.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse<String> handleUnauthorizedException(UnauthorizedException ex) {
    return new ErrorResponse<>(ex.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ErrorResponse<String> handleCouldNotSaveException(CouldNotSaveException ex) {
    return new ErrorResponse<>(ex.getMessage());
  }
}
