package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ItemListEntryMapperImpl.class, UserMapperImpl.class, WarehouseMapperImpl.class
})
class WarehouseMapperTest {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ItemListEntryMapper itemListEntryMapper;

    private final User user = User.builder()
            .id(UUID.randomUUID())
            .username("useros")
            .fullName("Sample user")
            .build();

    @Test
    void warehouseToBasicWarehouseRestDTO() {
        long id = 1L;
        String name = "Lidl warehouse";
        long capacity = 500;
        long itemsCapacitySize = 350;
        Warehouse warehouse = Warehouse.builder()
                .id(id)
                .name(name)
                .capacity(capacity)
                .users(List.of(user))
                .itemsCapacitySize(itemsCapacitySize)
                .build();
        BasicWarehouseRestDTO expectedResponse = new BasicWarehouseRestDTO(id, capacity, name,
                itemsCapacitySize);

        BasicWarehouseRestDTO result = warehouseMapper.warehouseToBasicWarehouseRestDTO(warehouse);

        assertThat(result).isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    void warehouseToFullWarehouseRestDTO() {
        long warehouseId = 1L;
        String warehouseName = "Lidl warehouse";
        long warehouseCapacity = 500;
        long itemsCapacitySize = 350;

        Item item = Item.builder()
                .id(1L)
                .size(50L)
                .name("name")
                .build();
        ItemListEntry itemListEntry = ItemListEntry.builder()
                .id(1L)
                .item(item)
                .quantity(50L)
                .build();
        Warehouse warehouse = Warehouse.builder()
                .id(warehouseId)
                .name(warehouseName)
                .capacity(warehouseCapacity)
                .users(List.of(user))
                .itemsCapacitySize(itemsCapacitySize)
                .entries(List.of(itemListEntry))
                .build();
        WarehouseItemRestDTO itemRestDTO = itemListEntryMapper.itemListEntryToWarehouseItemRestDTO(
                itemListEntry);
        UserRestDTO userRestDTO = userMapper.userToUserRestDTO(user);
        FullWarehouseRestDTO expectedResponse = new FullWarehouseRestDTO(warehouseId, warehouseCapacity,
                warehouseName, itemsCapacitySize, List.of(itemRestDTO), List.of(userRestDTO));

        FullWarehouseRestDTO result = warehouseMapper.warehouseToFullWarehouseRestDTO(warehouse);

        assertThat(result).isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    void warehouseRequestRestDTOToWarehouse() {
        long capacity = 500;
        String name = "Lidl warehouse";
        WarehouseRequestRestDTO requestRestDTO = new WarehouseRequestRestDTO(capacity, name);
        Warehouse expectedResult = Warehouse.builder()
                .name(name)
                .capacity(capacity)
                .build();

        Warehouse result = warehouseMapper.warehouseRequestRestDTOToWarehouse(requestRestDTO);

        assertThat(result).isNotNull()
                .isEqualTo(expectedResult);
    }
}
