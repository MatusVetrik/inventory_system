package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ItemService {

    @NonNull
    Item findById(@NotNull Long itemId);

    @NonNull
    List<Item> findAll();

    @NonNull
    List<Item> findItemsInWarehouse(@NotNull Long warehouseId);

    @NonNull
    Item findItemInWarehouse(@NotNull Long warehouseId, @NotNull Long itemId);

    @NonNull
    Item createItem(@NotNull Long warehouseId, @NotNull WarehouseItemRequestRestDTO requestRestDTO);

    @NonNull
    Item updateItem(@NotNull Long itemId, @NotNull Long warehouseId,
                    @NotNull WarehouseItemRequestRestDTO updateRequestRestDTO);

    void deleteItem(@NotNull Long itemId);

    void deleteWarehouseItem(@NotNull Long itemId, @NotNull Long warehouseId);
}
