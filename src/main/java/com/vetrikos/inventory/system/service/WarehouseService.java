package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.BasicWarehouse;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseUpdateRequestRestDTO;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

public interface WarehouseService {

  String WAREHOUSE_NOT_FOUND_STRING_FORMAT = "Warehouse with id %d not found";

  static String warehouseNotFoundMessage(Long warehouseId) {
    return String.format(WAREHOUSE_NOT_FOUND_STRING_FORMAT, warehouseId);
  }

  @NonNull
  Warehouse findById(@NotNull Long warehouseId);

  @NonNull
  @lombok.NonNull
  List<BasicWarehouse> findAll();

  @NonNull
  Warehouse createWarehouse(@NotNull WarehouseRequestRestDTO requestRestDTO);

  @NonNull
  Warehouse updateWarehouse(@NotNull Long warehouseId,
      @NotNull WarehouseUpdateRequestRestDTO updateRequestRestDTO);

  void deleteWarehouse(@NotNull Long warehouseId);

  void deleteUserFromWarehouse(@NotNull Long warehouseId, @NotNull UUID userId);

  void addUserToWarehouse(@NotNull Long warehouseId, @NotNull UUID userId);

}
