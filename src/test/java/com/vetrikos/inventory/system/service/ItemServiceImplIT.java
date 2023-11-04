package com.vetrikos.inventory.system.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.vetrikos.inventory.system.config.CustomPostgreSQLContainer;
import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.ItemRepository;
import com.vetrikos.inventory.system.repository.UserRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceImplIT {
  @ClassRule
  public static CustomPostgreSQLContainer postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private WarehouseRepository warehouseRepository;

  @Autowired
  private ItemListEntryRepository itemListEntryRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private WarehouseServiceImpl warehouseService;

  @Autowired
  private ItemServiceImpl itemService;

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

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    warehouseRepository.deleteAll();
    itemListEntryRepository.deleteAll();
    itemRepository.deleteAll();
  }

  @Test
  void shouldFindAll() {
    Warehouse warehouse = Warehouse.builder()
        .capacity(100)
        .name("Warehouse")
        .users(List.of(sampleUser))
        .build();
    Item item = Item.builder()
        .name("item1")
        .size(50)
        .build();
    ItemListEntry itemListEntry = ItemListEntry.builder().quantity(50).warehouse(warehouse).item(item).build();
    item.setEntries(Collections.singletonList(itemListEntry));

    item = itemListEntryRepository.save(itemListEntry).getItem();
    List<Item> result = itemService.findAll();
    System.out.println(result);
    System.out.println(sampleItem.toString());
    assertThat(result).isNotEmpty()
        .hasSize(1)
        .containsExactlyInAnyOrder(item);
  }

  @Test
  void shouldFindById() {
    sampleItem = itemRepository.save(sampleItem);
    Item result = itemService.findById(sampleItem.getId());
    assertThat(result).isNotNull()
        .isEqualTo(sampleItem);
  }
}
