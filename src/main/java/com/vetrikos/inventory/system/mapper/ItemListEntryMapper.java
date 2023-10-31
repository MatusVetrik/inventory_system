package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemListEntryMapper {

  @Mapping(target = "name", source = "item.name")
  @Mapping(target = "size", source = "item.size")
  WarehouseItemRestDTO itemListEntryToWarehouseItemRestDTO(ItemListEntry itemListEntry);
}
