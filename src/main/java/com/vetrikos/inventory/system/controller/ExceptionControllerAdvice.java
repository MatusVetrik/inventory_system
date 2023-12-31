package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.exception.ItemExceedsWarehouseCapacityException;
import com.vetrikos.inventory.system.exception.ItemNotFoundException;
import com.vetrikos.inventory.system.exception.UserNotFoundException;
import com.vetrikos.inventory.system.exception.WarehouseHasItemsException;
import com.vetrikos.inventory.system.exception.WarehouseNotFoundException;
import com.vetrikos.inventory.system.model.BadRequestErrorsInnerRestDTO;
import com.vetrikos.inventory.system.model.BadRequestRestDTO;
import com.vetrikos.inventory.system.model.ErrorRestDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

  private static final String INTERNAL_SERVER_ERROR_RESPONSE_MESSAGE = "Unexpected server error!";

  @ExceptionHandler(value = {AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  protected ResponseEntity<Object> handleAccessDeniedException(RuntimeException exception,
      HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    String requestURI = request.getRequestURI();

    if (principal != null) {
      log.error("Access denied on path: {}, for principal id: {}, : {}", requestURI,
          principal.getName(), principal);
    } else {
      log.error("Access denied on path: {}", requestURI);
    }

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorRestDTO(HttpStatus.FORBIDDEN.value(), exception.getMessage()));
  }

  @ExceptionHandler(value = {AuthenticationException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  protected ResponseEntity<Object> handleUnauthorizedException(RuntimeException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorRestDTO(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    log.error(exception.getMessage());
    List<BadRequestErrorsInnerRestDTO> errors = exception.getBindingResult().getAllErrors()
        .stream()
        .map(objectError -> new BadRequestErrorsInnerRestDTO(objectError.getDefaultMessage())
            .fieldName(((FieldError) objectError).getField()))
        .toList();

    return ResponseEntity.badRequest().body(new BadRequestRestDTO(errors));
  }

  @ExceptionHandler(value = {ItemExceedsWarehouseCapacityException.class,
      HttpMessageNotReadableException.class, WarehouseHasItemsException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleBadRequestException(
      RuntimeException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.badRequest()
        .body(new ErrorRestDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
  }

  @ExceptionHandler(value = {UserNotFoundException.class, WarehouseNotFoundException.class,
      ItemNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ResponseEntity<Object> handleUserWarehouseItemNotFoundException(
      @NonNull RuntimeException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorRestDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
  }

  @ExceptionHandler(value = {Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Object> handleException(Exception exception) {
    log.error(exception.getMessage(), exception);
    return new ResponseEntity<>(
        new ErrorRestDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            INTERNAL_SERVER_ERROR_RESPONSE_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
