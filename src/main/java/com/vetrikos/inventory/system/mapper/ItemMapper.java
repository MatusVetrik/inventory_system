package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "entries", ignore = true)
  Item warehouseItemRequestRestDTOToItem(WarehouseItemRequestRestDTO warehouseItemRequestRestDTO);
}
