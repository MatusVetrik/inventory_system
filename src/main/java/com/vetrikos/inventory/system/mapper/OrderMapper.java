package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Order;
import com.vetrikos.inventory.system.model.OrderRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class, WarehouseMapper.class, ItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "sourceId", source = "fromWarehouse.name")
    @Mapping(target = "destinationId", source = "toWarehouse.name")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userToUuid")
    OrderRestDTO orderToOrderDTO(Order order);

    @Mapping(target = "fromWarehouse.name", source = "sourceId")
    @Mapping(target = "toWarehouse.name", source = "destinationId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "uuidToUser")
    Order orderDTOToOrder(OrderRestDTO orderDTO);
}
