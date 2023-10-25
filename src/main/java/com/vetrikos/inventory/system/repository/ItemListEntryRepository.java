package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.ItemListEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemListEntryRepository extends JpaRepository<ItemListEntry, Long> {

}
