package com.vetrikos.inventory.system.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    ItemMapperImpl.class,
})
class ItemMapperTest {

  @Autowired
  private ItemMapper itemMapper;

  private Item sampleItem;
  private User sampleUser;
  @BeforeEach
  void setUp() {
    sampleItem = Item.builder()
        .name("item1")
        .size(50)
        .entries(new ArrayList<>())
        .build();
    sampleUser = User.builder()
        .username("user")
        .fullName("Sample User")
        .build();
  }


  @Test
  void warehouseItemRequestRestDTOToItem() {
    int size = 500;
    String name = "Apple";
    WarehouseItemRequestRestDTO requestRestDTO = new WarehouseItemRequestRestDTO(name, size);
    Item expectedItem = Item.builder()
        .name(name)
        .size(size)
        .build();

    Item result = itemMapper.warehouseItemRequestRestDTOToItem(requestRestDTO);

    assertThat(result).isNotNull()
        .isEqualTo(expectedItem);
  }

  @Test
  void itemToWarehouseItemRestDTO() {
    Warehouse warehouse = Warehouse.builder()
        .capacity(100)
        .name("Warehouse")
        .users(List.of(sampleUser))
        .build();
    long itemId = 1L;
    String itemName = "item1";
    int itemSize = 50;
    Item item = Item.builder()
        .id(itemId)
        .name(itemName)
        .size(itemSize)
        .build();
    ItemListEntry itemListEntry = ItemListEntry.builder().quantity(itemSize).warehouse(warehouse).item(item).build();
    ItemListEntry itemListEntry2 = ItemListEntry.builder().quantity(100).warehouse(warehouse).item(item).build();
    item.setEntries(List.of(itemListEntry, itemListEntry2));
    WarehouseItemRestDTO expectedResult = new WarehouseItemRestDTO(itemId, itemName, itemSize, 150);

    WarehouseItemRestDTO result = itemMapper.itemToWarehouseItemRestDTO(item);

    assertThat(result).isNotNull()
        .isEqualTo(expectedResult);
  }
}