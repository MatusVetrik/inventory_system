package com.vetrikos.inventory.system.exception;

public class ItemNotFoundException extends RuntimeException {

  public static final String ITEM_NOT_FOUND_STRING_FORMAT = "Item with id %s not found";
  public static final String ITEM_IN_WAREHOUSE_NOT_FOUND_STRING_FORMAT = "Item with id %s not found in warehouse with id %s";

  public ItemNotFoundException(Long itemId) {
    super(itemNotFoundMessage(itemId));
  }

  public ItemNotFoundException(Long itemId, Long warehouseId) {
    super(itemInWarehouseNotFoundMessage(itemId, warehouseId));
  }

  public static String itemNotFoundMessage(Long itemId) {
    return String.format(ITEM_NOT_FOUND_STRING_FORMAT, itemId);
  }

  public static String itemInWarehouseNotFoundMessage(Long itemId, Long warehouseId) {
    return String.format(ITEM_IN_WAREHOUSE_NOT_FOUND_STRING_FORMAT, itemId, warehouseId);
  }

}
