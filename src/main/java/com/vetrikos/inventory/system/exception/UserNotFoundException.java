package com.vetrikos.inventory.system.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

  public static final String USER_NOT_FOUND_STRING_FORMAT = "User with id %s not found";
  public static final String USER_NOT_FOUND_WITHIN_WAREHOUSE_STRING_FORMAT = "User with id %s not found in warehouse with id: %d";

  public UserNotFoundException(UUID userId) {
    super(userNotFoundMessage(userId));
  }

  public UserNotFoundException(UUID userId, Long warehouseId) {
    super(userNotFoundWithinWarehouseMessage(userId, warehouseId));
  }

  public static String userNotFoundMessage(UUID userId) {
    return String.format(USER_NOT_FOUND_STRING_FORMAT, userId.toString());
  }

  public static String userNotFoundWithinWarehouseMessage(UUID userId, Long warehouseId) {
    return String.format(USER_NOT_FOUND_WITHIN_WAREHOUSE_STRING_FORMAT, userId.toString(),
        warehouseId);
  }
}
