package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.BasicWarehouseRestDTO;
import com.vetrikos.inventory.system.model.FullWarehouseRestDTO;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class, ItemListEntryMapper.class})
public interface WarehouseMapper {

  BasicWarehouseRestDTO warehouseToBasicWarehouseRestDTO(Warehouse warehouse);

  @Mapping(target = "items", source = "entries")
  FullWarehouseRestDTO warehouseToFullWarehouseRestDTO(Warehouse warehouse);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "entries", ignore = true)
  @Mapping(target = "receivedOrders", ignore = true)
  @Mapping(target = "sendOrders", ignore = true)
  Warehouse warehouseRequestRestDTOToWarehouse(WarehouseRequestRestDTO requestRestDTO);

}
