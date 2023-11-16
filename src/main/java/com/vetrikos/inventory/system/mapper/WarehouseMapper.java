package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.BasicWarehouse;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.BasicWarehouseRestDTO;
import com.vetrikos.inventory.system.model.FullWarehouseRestDTO;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class, ItemListEntryMapper.class})
public interface WarehouseMapper {

  @Mapping(target = "itemsCapacitySize", defaultValue = "0L")
  BasicWarehouseRestDTO warehouseToBasicWarehouseRestDTO(Warehouse warehouse);

  @Mapping(target = "itemsCapacitySize", defaultValue = "0L")
  BasicWarehouseRestDTO basicWarehouseToBasicWarehouseRestDTO(BasicWarehouse basicWarehouse);

  @Mapping(target = "items", source = "entries")
  @Mapping(target = "itemsCapacitySize", defaultValue = "0L")
  FullWarehouseRestDTO warehouseToFullWarehouseRestDTO(Warehouse warehouse);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "entries", ignore = true)
  @Mapping(target = "receivedOrders", ignore = true)
  @Mapping(target = "sendOrders", ignore = true)
  Warehouse warehouseRequestRestDTOToWarehouse(WarehouseRequestRestDTO requestRestDTO);

}
