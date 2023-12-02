package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.WarehouseItemsApi;
import com.vetrikos.inventory.system.config.SecurityConfiguration.ConfigAnonUserRoles.Fields;
import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.mapper.ItemMapper;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import com.vetrikos.inventory.system.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WarehouseItemController implements WarehouseItemsApi {

  private final ItemService itemService;
  private final ItemMapper itemMapper;

  @Override
  @PreAuthorize("hasRole('" + Fields.ROLE_ADMIN
      + "') || @warehouseRepository.checkUserExistsInWarehouse(T(java.util.UUID).fromString(authentication.name), #warehouseId)")
  public ResponseEntity<WarehouseItemRestDTO> createWarehouseItem(Long warehouseId,
      WarehouseItemRequestRestDTO warehouseItemRequestRestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(itemMapper.itemToWarehouseItemRestDTO(
            itemService.createItem(warehouseId, warehouseItemRequestRestDTO)));
  }

  @Override
  @PreAuthorize("hasAnyRole('" + Fields.ROLE_ADMIN + "','" + Fields.ROLE_MANAGER + "')")
  public ResponseEntity<Void> deleteWarehouseItem(Long warehouseId, Long itemId) {
    itemService.deleteWarehouseItem(itemId, warehouseId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<WarehouseItemRestDTO> getWarehouseItemById(Long warehouseId, Long itemId) {
    Item item = itemService.findItemInWarehouse(warehouseId, itemId);
    WarehouseItemRestDTO warehouseItemDTO = itemMapper.itemToWarehouseItemRestDTO(item);
    return ResponseEntity.ok(warehouseItemDTO);
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<WarehouseItemRestDTO>> listWarehouseItems(Long warehouseId) {
    List<Item> items = itemService.findItemsInWarehouse(warehouseId);
    List<WarehouseItemRestDTO> warehouseItemDTOs = items.stream()
        .map(itemMapper::itemToWarehouseItemRestDTO)
        .toList();
    return ResponseEntity.ok(warehouseItemDTOs);
  }

  @Override
  @PreAuthorize("hasRole('" + Fields.ROLE_ADMIN
      + "') || @warehouseRepository.checkUserExistsInWarehouse(T(java.util.UUID).fromString(authentication.name), #warehouseId)")
  public ResponseEntity<WarehouseItemRestDTO> updateWarehouseItem(Long warehouseId, Long itemId,
      WarehouseItemRequestRestDTO warehouseItemRequestRestDTO) {
    Item updatedItem = itemService.updateItem(itemId, warehouseId, warehouseItemRequestRestDTO);
    WarehouseItemRestDTO updatedItemDTO = itemMapper.itemToWarehouseItemRestDTO(updatedItem);
    return ResponseEntity.ok(updatedItemDTO);
  }
}
