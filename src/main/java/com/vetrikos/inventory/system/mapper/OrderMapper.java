package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Order;
import com.vetrikos.inventory.system.model.OrderResponseRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class})
public interface OrderMapper {

    @Mapping(target = "sourceId", source = "fromWarehouse.id")
    @Mapping(target = "sourceName", source = "fromWarehouse.name")
    @Mapping(target = "destinationId", source = "toWarehouse.id")
    @Mapping(target = "destinationName", source = "toWarehouse.name")
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "itemName", source = "item.name")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userToUuid")
    @Mapping(target = "createdByName", source = "createdBy.username")
    OrderResponseRestDTO orderToOrderResponseDTO(Order order);

}
