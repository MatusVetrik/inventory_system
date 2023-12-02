package com.vetrikos.inventory.system.exception;

public class WarehouseHasItemsException extends RuntimeException{
  public static final String WAREHOUSE_HAS_ITEMS_STRING_FORMAT = "Warehouse with %d items, delete those first";

  public WarehouseHasItemsException(int itemCount) {
    super(warehouseHasItemsMessage(itemCount));
  }

  public static String warehouseHasItemsMessage(int itemcount) {
    return String.format(WAREHOUSE_HAS_ITEMS_STRING_FORMAT, itemcount);
  }

}
