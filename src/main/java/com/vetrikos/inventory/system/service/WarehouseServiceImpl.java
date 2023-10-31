package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseUpdateRequestRestDTO;
import com.vetrikos.inventory.system.repository.UserRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {


  private final WarehouseRepository warehouseRepository;
  private final UserRepository userRepository;

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
    User user = userRepository.findById(requestRestDTO.getUserId())
        .orElseThrow(() -> new IllegalArgumentException(
            UserService.userNotFoundMessage(requestRestDTO.getUserId())));

    Warehouse warehouse = Warehouse.builder()
        // TODO: set name when openapi dto changes
        .name("name")
        .capacity(requestRestDTO.getCapacity())
        .users(List.of(user))
        .build();
    user.setWarehouse(warehouse);
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

    warehouse.setCapacity(updateRequestRestDTO.getCapacity());
    // TODO: set name when openapi dto changes
    warehouse.setName("new name");

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
