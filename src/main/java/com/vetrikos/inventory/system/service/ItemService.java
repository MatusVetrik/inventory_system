package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.lang.NonNull;

public interface ItemService {
  String ITEM_NOT_FOUND_STRING_FORMAT = "Item with id %s not found";
  static String itemNotFoundMessage(Long itemId) {
    return String.format(ITEM_NOT_FOUND_STRING_FORMAT, itemId);
  }
  @NonNull
  Item findById(@NotNull Long itemId);

  @NonNull
  List<Item> findAll();

  @NonNull
  List<Item> findItemsInWarehouse(@NotNull Long warehouseId);

  @NonNull
  Item findItemInWarehouse(@NotNull Long warehouseId,@NotNull Long itemId);
  @NonNull
  Item createItem(@NotNull Long warehouseId, @NotNull WarehouseItemRestDTO requestRestDTO);

  @NonNull
  Item updateItem(@NotNull Long itemId, @NotNull Long warehouseId,
      @NotNull WarehouseItemRequestRestDTO updateRequestRestDTO);

  void deleteItem(@NotNull Long itemId);

  void deleteWarehouseItem(@NotNull Long itemId,@NotNull Long warehouseId);
}
