package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.WarehousesApi;
import com.vetrikos.inventory.system.mapper.WarehouseMapper;
import com.vetrikos.inventory.system.model.BasicWarehouseRestDTO;
import com.vetrikos.inventory.system.model.FullWarehouseRestDTO;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseUpdateRequestRestDTO;
import com.vetrikos.inventory.system.service.WarehouseService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WarehouseController implements WarehousesApi {

  private final WarehouseService warehouseService;
  private final WarehouseMapper warehouseMapper;

  @Override
  public ResponseEntity<FullWarehouseRestDTO> createWarehouse(
      WarehouseRequestRestDTO warehouseRequestRestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(warehouseMapper.warehouseToFullWarehouseRestDTO(
            warehouseService.createWarehouse(warehouseRequestRestDTO)));
  }

  @Override
  public ResponseEntity<Void> deleteWarehouse(Long warehouseId) {
    warehouseService.deleteWarehouse(warehouseId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<FullWarehouseRestDTO> getWarehouseById(Long warehouseId) {
    return ResponseEntity.ok(
        warehouseMapper.warehouseToFullWarehouseRestDTO(warehouseService.findById(warehouseId)));
  }

  @Override
  public ResponseEntity<List<BasicWarehouseRestDTO>> listWarehouses() {
    return ResponseEntity.ok(warehouseService.findAll().stream()
        .map(warehouseMapper::warehouseToBasicWarehouseRestDTO)
        .collect(Collectors.toList()));
  }

  @Override
  public ResponseEntity<FullWarehouseRestDTO> updateWarehouse(Long warehouseId,
      WarehouseUpdateRequestRestDTO warehouseUpdateRequestRestDTO) {
    return ResponseEntity.ok(warehouseMapper.warehouseToFullWarehouseRestDTO(
        warehouseService.updateWarehouse(warehouseId, warehouseUpdateRequestRestDTO)));
  }
}
