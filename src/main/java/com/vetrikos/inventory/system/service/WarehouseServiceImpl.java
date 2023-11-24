package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.BasicWarehouse;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.exception.ItemExceedsWarehouseCapacityException;
import com.vetrikos.inventory.system.exception.UserNotFoundException;
import com.vetrikos.inventory.system.exception.WarehouseNotFoundException;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseUpdateRequestRestDTO;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.UserRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

  private final WarehouseRepository warehouseRepository;
  private final UserRepository userRepository;
  private final ItemListEntryRepository itemListEntryRepository;

  @Override
  @NonNull
  public Warehouse findById(Long warehouseId) {
    return warehouseRepository.findById(warehouseId).map(warehouse -> {
          warehouse.setItemsCapacitySize(
              itemListEntryRepository.getActualItemsCapacitySize(warehouseId));
          return warehouse;
        })
        .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));
  }

  @Override
  @NonNull
  public List<BasicWarehouse> findAll() {
    return warehouseRepository.findAllBasicWarehouses();
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
        .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

    if (updateRequestRestDTO.getCapacity() != null) {
      Long actualItemsCapacitySize = itemListEntryRepository.getActualItemsCapacitySize(warehouseId);

      if (updateRequestRestDTO.getCapacity() < actualItemsCapacitySize) {
        throw new ItemExceedsWarehouseCapacityException(
            "New warehouse capacity can not be lower than current items capacity");
      }

      warehouse.setCapacity(updateRequestRestDTO.getCapacity());
    }

    if (updateRequestRestDTO.getName() != null || !updateRequestRestDTO.getName().isBlank()) {
      warehouse.setName(updateRequestRestDTO.getName());
    }

    return warehouse;
  }

  @Override
  @Transactional
  public void addUserToWarehouse(Long warehouseId, UUID userId) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    user.setWarehouse(warehouse);
    warehouse.getUsers().add(user);
  }

  @Override
  @Transactional
  public void deleteUserFromWarehouse(Long warehouseId, UUID userId) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

    User user = warehouse.getUsers().stream()
        .filter(u -> u.getId().equals(userId))
        .findFirst().orElseThrow(() -> new UserNotFoundException(userId, warehouseId));

    user.setWarehouse(null);
    warehouse.getUsers().remove(user);
  }

  @Override
  @Transactional
  public void deleteWarehouse(Long warehouseId) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));
    warehouse.getUsers().forEach(user -> user.setWarehouse(null));
    warehouseRepository.delete(warehouse);
  }
}
