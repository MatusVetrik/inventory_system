package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.exception.ItemExceedsWarehouseCapacityException;
import com.vetrikos.inventory.system.exception.ItemNotFoundException;
import com.vetrikos.inventory.system.exception.UserNotFoundException;
import com.vetrikos.inventory.system.exception.WarehouseHasItemsException;
import com.vetrikos.inventory.system.exception.WarehouseNotFoundException;
import com.vetrikos.inventory.system.model.BadRequestErrorsInnerRestDTO;
import com.vetrikos.inventory.system.model.BadRequestRestDTO;
import com.vetrikos.inventory.system.model.ErrorRestDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

  private static final String INTERNAL_SERVER_ERROR_RESPONSE_MESSAGE = "Unexpected server error!";

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    log.error(exception.getMessage(), exception);
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
    log.error(exception.getMessage(), exception);
    return ResponseEntity.badRequest()
        .body(new ErrorRestDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
  }

  @ExceptionHandler(value = {UserNotFoundException.class, WarehouseNotFoundException.class,
      ItemNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ResponseEntity<Object> handleUserWarehouseItemNotFoundException(
      @NonNull RuntimeException exception) {
    log.error(exception.getMessage(), exception);
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
