package com.vetrikos.inventory.system.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.BasicWarehouseRestDTO;
import com.vetrikos.inventory.system.model.FullWarehouseRestDTO;
import com.vetrikos.inventory.system.model.UserRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    int capacity = 500;
    Warehouse warehouse = Warehouse.builder()
        .id(id)
        .name(name)
        .capacity(capacity)
        .users(List.of(user))
        .build();
    BasicWarehouseRestDTO expectedResponse = new BasicWarehouseRestDTO(id, capacity, name);

    BasicWarehouseRestDTO result = warehouseMapper.warehouseToBasicWarehouseRestDTO(warehouse);

    assertThat(result).isNotNull()
        .isEqualTo(expectedResponse);
  }

  @Test
  void warehouseToFullWarehouseRestDTO() {
    long warehouseId = 1L;
    String warehouseName = "Lidl warehouse";
    int warehouseCapacity = 500;

    Item item = Item.builder()
        .id(1L)
        .size(50)
        .name("name")
        .build();
    ItemListEntry itemListEntry = ItemListEntry.builder()
        .id(1L)
        .item(item)
        .quantity(50)
        .build();
    Warehouse warehouse = Warehouse.builder()
        .id(warehouseId)
        .name(warehouseName)
        .capacity(warehouseCapacity)
        .users(List.of(user))
        .entries(List.of(itemListEntry))
        .build();
    WarehouseItemRestDTO itemRestDTO = itemListEntryMapper.itemListEntryToWarehouseItemRestDTO(itemListEntry);
    UserRestDTO userRestDTO = userMapper.userToUserRestDTO(user);
    FullWarehouseRestDTO expectedResponse = new FullWarehouseRestDTO(warehouseId, warehouseCapacity,
        warehouseName, List.of(itemRestDTO), List.of(userRestDTO));


    FullWarehouseRestDTO result = warehouseMapper.warehouseToFullWarehouseRestDTO(warehouse);

    assertThat(result).isNotNull()
        .isEqualTo(expectedResponse);
  }

  @Test
  void warehouseRequestRestDTOToWarehouse() {
    int capacity = 500;
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
