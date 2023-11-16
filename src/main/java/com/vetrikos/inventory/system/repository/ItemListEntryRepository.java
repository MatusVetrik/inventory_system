package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.Warehouse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemListEntryRepository extends JpaRepository<ItemListEntry, Long> {

  List<ItemListEntry> findItemListEntriesByWarehouseAndItem(Warehouse warehouse, Item item);

  List<ItemListEntry> findItemListEntriesByWarehouse(Warehouse warehouse);

  @Query(value = "SELECT COALESCE(SUM(e.quantity * i.size), 0) FROM ItemListEntry e JOIN e.item i WHERE e.warehouse.id = :warehouseId")
  Long getActualItemsCapacitySize(@Param("warehouseId") Long warehouseId);

}
