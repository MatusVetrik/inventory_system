package com.vetrikos.inventory.system.exception;

public class WarehouseNotFoundException extends RuntimeException {
  public static final String WAREHOUSE_NOT_FOUND_STRING_FORMAT = "Warehouse with id %d not found";

  public WarehouseNotFoundException(Long warehouseId) {
    super(warehouseNotFoundMessage(warehouseId));
  }

  public static String warehouseNotFoundMessage(Long warehouseId) {
    return String.format(WAREHOUSE_NOT_FOUND_STRING_FORMAT, warehouseId);
  }

}
