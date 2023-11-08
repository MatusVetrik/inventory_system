package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.WarehouseItemsApi;
import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.mapper.ItemMapper;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import com.vetrikos.inventory.system.service.ItemService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WarehouseItemController implements WarehouseItemsApi {

  private final ItemService itemService;
  private final ItemMapper itemMapper;

  @Override
  public ResponseEntity<WarehouseItemRestDTO> createWarehouseItem(Long warehouseId,
      WarehouseItemRequestRestDTO warehouseItemRequestRestDTO) {
    Item item = itemService.createItem(warehouseId,warehouseItemRequestRestDTO);
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteWarehouseItem(Long warehouseId, Long itemId) {
    itemService.deleteWarehouseItem(itemId, warehouseId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<WarehouseItemRestDTO> getWarehouseItemById(Long warehouseId, Long itemId) {
    Item item = itemService.findItemInWarehouse(warehouseId, itemId);
    WarehouseItemRestDTO warehouseItemDTO = itemMapper.itemToWarehouseItemRestDTO(item);
    return ResponseEntity.ok(warehouseItemDTO);
  }

  @Override
  public ResponseEntity<List<WarehouseItemRestDTO>> listWarehouseItems(Long warehouseId) {
    List<Item> items = itemService.findItemsInWarehouse(warehouseId);
    List<WarehouseItemRestDTO> warehouseItemDTOs = items.stream()
        .map(itemMapper::itemToWarehouseItemRestDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(warehouseItemDTOs);
  }

  @Override
  public ResponseEntity<WarehouseItemRestDTO> updateWarehouseItem(Long warehouseId, Long itemId,
      WarehouseItemRequestRestDTO warehouseItemRequestRestDTO) {
    // Updating an item in the warehouse
    Item updatedItem = itemService.updateItem(itemId, warehouseId, warehouseItemRequestRestDTO);
    WarehouseItemRestDTO updatedItemDTO = itemMapper.itemToWarehouseItemRestDTO(updatedItem);
    return ResponseEntity.ok(updatedItemDTO);
  }
}
