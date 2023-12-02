package com.vetrikos.inventory.system.exception;

public class ItemExceedsWarehouseCapacityException extends RuntimeException {

  public static final String ITEM_EXCEEDS_CAPACITY_STRING_FORMAT = "Item exceeds warehouse capacity by %s";
  public static final String NOT_ENOUGH_ITEMS_IN_WAREHOUSE_STRING_FORMAT = "%s items missing in warehouse with ID %s";

  public ItemExceedsWarehouseCapacityException(Long itemCapacity) {
    super(itemExceedsCapacityMessage(itemCapacity));
  }
  public ItemExceedsWarehouseCapacityException(Long itemQuantity, Long warehouseId) {
    super(notEnoughItemsInWarehouseMessage(itemQuantity,warehouseId));
  }
  public ItemExceedsWarehouseCapacityException(String message) {
    super(message);
  }

  public static String itemExceedsCapacityMessage(Long itemCapacity) {
    return String.format(ITEM_EXCEEDS_CAPACITY_STRING_FORMAT, itemCapacity);
  }
  public static String notEnoughItemsInWarehouseMessage(Long itemQuantity, Long warehouseId) {
    return String.format(NOT_ENOUGH_ITEMS_IN_WAREHOUSE_STRING_FORMAT, itemQuantity,warehouseId);
  }
}
