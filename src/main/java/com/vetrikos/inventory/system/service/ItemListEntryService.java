package com.vetrikos.inventory.system.service;

public interface ItemListEntryService {
  String ITEM_IN_WAREHOUSE_NOT_FOUND_STRING_FORMAT = "Item with id %s not found in warehouse with id %s";
  static String itemInWarehouseNotFoundMessage(Long itemId, Long warehouseId) {
    return String.format(ITEM_IN_WAREHOUSE_NOT_FOUND_STRING_FORMAT, itemId, warehouseId);
  }
}
