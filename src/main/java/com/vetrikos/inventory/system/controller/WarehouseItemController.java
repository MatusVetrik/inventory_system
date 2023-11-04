package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.WarehouseItemsApi;
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
    return WarehouseItemsApi.super.createWarehouseItem(warehouseId, warehouseItemRequestRestDTO);
  }

  @Override
  public ResponseEntity<Void> deleteWarehouseItem(Long warehouseId, Long itemId) {
    return WarehouseItemsApi.super.deleteWarehouseItem(warehouseId, itemId);
  }

  @Override
  public ResponseEntity<WarehouseItemRestDTO> getWarehouseItemById(Long warehouseId, Long itemId) {
    return WarehouseItemsApi.super.getWarehouseItemById(warehouseId, itemId);
  }

  @Override
  public ResponseEntity<List<WarehouseItemRestDTO>> listWarehouseItems(Long warehouseId) {
    return ResponseEntity.ok(
        itemService.findItemsInWarehouse(warehouseId).stream()
            .map(itemMapper::itemToWarehouseItemRestDTO).collect(
                Collectors.toList()));
  }

  @Override
  public ResponseEntity<WarehouseItemRestDTO> updateWarehouseItem(Long warehouseId, Long itemId,
      WarehouseItemRequestRestDTO warehouseItemRequestRestDTO) {
    return WarehouseItemsApi.super.updateWarehouseItem(warehouseId, itemId,
        warehouseItemRequestRestDTO);
  }
}
