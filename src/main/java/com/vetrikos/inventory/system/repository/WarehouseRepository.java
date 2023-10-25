package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}
