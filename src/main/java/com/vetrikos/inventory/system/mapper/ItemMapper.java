package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "entries", ignore = true)
  Item warehouseItemRequestRestDTOToItem(WarehouseItemRequestRestDTO warehouseItemRequestRestDTO);

  @Mapping(target = "quantity", source = "entries")
  WarehouseItemRestDTO itemToWarehouseItemRestDTO(Item item);

  default Integer itemListEntriesToQuantity(List<ItemListEntry> itemListEntries) {
    return itemListEntries.stream()
        .map(ItemListEntry::getQuantity)
        .reduce(0, Integer::sum);
  }
}
