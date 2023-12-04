package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entries", ignore = true)
    Item warehouseItemRequestRestDTOToItem(WarehouseItemRequestRestDTO warehouseItemRequestRestDTO);

    default WarehouseItemRestDTO itemToWarehouseItemRestDTO(Item item, Long warehouseId) {
        WarehouseItemRestDTO dto = new WarehouseItemRestDTO();
        Long quantity = item.getEntries().stream()
            .filter(entry -> entry.getWarehouse().getId().equals(warehouseId))
            .map(ItemListEntry::getQuantity)
            .reduce(0L, Long::sum);

        dto.setQuantity(quantity);
        dto.setName(item.getName());
        dto.setId(item.getId());
        dto.setSize(item.getSize());
        return dto;
    }
}
