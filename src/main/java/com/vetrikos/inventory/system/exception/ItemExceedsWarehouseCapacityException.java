package com.vetrikos.inventory.system.exception;

public class ItemExceedsWarehouseCapacityException extends RuntimeException {

  public static final String ITEM_EXCEEDS_CAPACITY_STRING_FORMAT = "Item exceeds warehouse capacity by %s";

  public ItemExceedsWarehouseCapacityException(Long itemCapacity) {
    super(itemExceedsCapacityMessage(itemCapacity));
  }

  public ItemExceedsWarehouseCapacityException(String message) {
    super(message);
  }

  public static String itemExceedsCapacityMessage(Long itemCapacity) {
    return String.format(ITEM_EXCEEDS_CAPACITY_STRING_FORMAT, itemCapacity);
  }
}
