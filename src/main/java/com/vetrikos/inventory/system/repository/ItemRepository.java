package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.Warehouse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
  // Item findItemByWarehouseAndId(Warehouse warehouse, Long itemId);

}
