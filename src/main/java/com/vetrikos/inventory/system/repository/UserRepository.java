package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  List<User> findAllByWarehouseId(Long warehouseId);

  Optional<User> findByIdAndWarehouseId(UUID userId, Long warehouseId);
}
