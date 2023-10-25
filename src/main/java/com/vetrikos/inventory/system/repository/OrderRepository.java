package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
