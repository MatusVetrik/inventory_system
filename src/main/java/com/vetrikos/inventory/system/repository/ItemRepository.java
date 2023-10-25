package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
