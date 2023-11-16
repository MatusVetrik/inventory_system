package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseUpdateRequestRestDTO;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

  private final WarehouseRepository warehouseRepository;
  private final ItemListEntryRepository itemListEntryRepository;

  @Override
  @NonNull
  public Warehouse findById(Long warehouseId) {
    return warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new IllegalArgumentException(
            WarehouseService.warehouseNotFoundMessage(warehouseId)));
  }

  @Override
  @NonNull
  public List<Warehouse> findAll() {
    return warehouseRepository.findAll();
  }

  @Override
  @NonNull
  @Transactional
  public Warehouse createWarehouse(WarehouseRequestRestDTO requestRestDTO) {
    // TODO: potom asi pridat to warehousu usera co ho vytvoril

    Warehouse warehouse = Warehouse.builder()
        .name(requestRestDTO.getName())
        .capacity(requestRestDTO.getCapacity())
        .build();
    return warehouseRepository.save(warehouse);
  }

  @Override
  @NonNull
  @Transactional
  public Warehouse updateWarehouse(Long warehouseId,
      WarehouseUpdateRequestRestDTO updateRequestRestDTO) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new IllegalArgumentException(
            WarehouseService.warehouseNotFoundMessage(warehouseId)));

    Long actualItemsCapacitySize = itemListEntryRepository.getActualItemsCapacitySize(warehouseId);

    if (updateRequestRestDTO.getCapacity() < actualItemsCapacitySize) {
      throw new IllegalArgumentException(
          "New warehouse capacity can not be lower than current items capacity");
    }

    warehouse.setCapacity(updateRequestRestDTO.getCapacity());
    warehouse.setName(updateRequestRestDTO.getName());

    return warehouse;
  }

  @Override
  @Transactional
  public void deleteWarehouse(Long warehouseId) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new IllegalArgumentException(
            WarehouseService.warehouseNotFoundMessage(warehouseId)));
    warehouse.getUsers().forEach(user -> user.setWarehouse(null));
    warehouseRepository.delete(warehouse);
  }
}
