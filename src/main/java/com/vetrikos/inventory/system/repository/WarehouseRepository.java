package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.BasicWarehouse;
import com.vetrikos.inventory.system.entity.Warehouse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

  @Query("SELECT NEW com.vetrikos.inventory.system.entity.BasicWarehouse("
      + "warehouse.id, "
      + "warehouse.capacity, "
      + "warehouse.name, "
      + "CAST(COALESCE(SUM(itemEntry.quantity * item.size), 0) AS Long)) "
      + "FROM Warehouse warehouse "
      + "LEFT JOIN warehouse.entries itemEntry "
      + "LEFT JOIN itemEntry.item item "
      + "GROUP BY warehouse.id")
  List<BasicWarehouse> findAllBasicWarehouses();
}
