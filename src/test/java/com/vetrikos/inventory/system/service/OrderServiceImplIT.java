package com.vetrikos.inventory.system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.vetrikos.inventory.system.config.CustomPostgreSQLContainer;
import com.vetrikos.inventory.system.config.IntegrationTest;
import com.vetrikos.inventory.system.config.InventoryKeycloakContainer;
import com.vetrikos.inventory.system.config.MockCustomJwtUser;
import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.Order;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.exception.ItemExceedsWarehouseCapacityException;
import com.vetrikos.inventory.system.model.OrderRestDTO;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.ItemRepository;
import com.vetrikos.inventory.system.repository.OrderRepository;
import com.vetrikos.inventory.system.repository.UserRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import com.vetrikos.inventory.system.config.CustomPostgreSQLContainer;
import com.vetrikos.inventory.system.config.IntegrationTest;
import com.vetrikos.inventory.system.config.InventoryKeycloakContainer;
import com.vetrikos.inventory.system.entity.*;
import com.vetrikos.inventory.system.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testcontainers.junit.jupiter.Container;

@IntegrationTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderServiceImplIT {

  @Container
  public static CustomPostgreSQLContainer postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

  @Container
  public static InventoryKeycloakContainer keycloakContainer = InventoryKeycloakContainer.getInstance();

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private WarehouseRepository warehouseRepository;

  @Autowired
  private ItemListEntryRepository itemListEntryRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private WarehouseServiceImpl warehouseService;

  @Autowired
  private ItemServiceImpl itemService;

  @Autowired
  private OrderServiceImpl orderService;

  private Item sampleItem;
  private User sampleUser;
  private Warehouse warehouse;
  private Warehouse warehouse2;
  private Warehouse warehouse3;

  @BeforeEach
  void setUp() {
    sampleItem = Item.builder()
        .name("item1")
        .size(50L)
        .entries(new ArrayList<>())
        .build();
    sampleUser = User.builder()
        .id(UUID.fromString("70c42715-ebd5-44bb-b1de-b74f44c6d110"))
        .username("user")
        .fullName("Sample User")
        .build();
    warehouse = Warehouse.builder()
        .capacity(2800L)
        .name("Warehouse")
        .users(List.of(sampleUser))
        .build();
    warehouse2 = Warehouse.builder()
        .capacity(2800L)
        .name("Warehouse 2")
        .users(List.of(sampleUser))
        .build();
    warehouse3 = Warehouse.builder()
        .capacity(2000L)
        .name("Warehouse 2")
        .users(List.of(sampleUser))
        .build();
  }

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
    userRepository.deleteAll();
    itemRepository.deleteAll();
    itemListEntryRepository.deleteAll();
    warehouseRepository.deleteAll();

  }

  @Test
  @MockCustomJwtUser
  void createOrder() {
    warehouse = warehouseRepository.save(warehouse);
    warehouse2 = warehouseRepository.save(warehouse2);

    sampleUser = userRepository.save(sampleUser);

    ItemListEntry itemListEntry = ItemListEntry.builder()
        .item(sampleItem)
        .warehouse(warehouse)
        .quantity(5L)
        .build();
    sampleItem.setEntries(Collections.singletonList(itemListEntry));
    sampleItem = itemRepository.save(sampleItem);
    ItemListEntry savedItemListEntry = itemListEntryRepository.save(itemListEntry);

    Order order = Order.builder()
        .createdBy(sampleUser)
        .item(sampleItem)
        .quantity(5L)
        .fromWarehouse(warehouse)
        .toWarehouse(warehouse2)
        .build();
    Order savedOrder = orderRepository.save(order);

    List<Order> result = orderService.findAllOrders();

    assertThat(result).isNotEmpty()
        .hasSize(1)
        .containsExactlyInAnyOrder(savedOrder);
  }

  @Test
  @MockCustomJwtUser
  void shouldNotCreateOrderNoItems() {
    warehouse = warehouseRepository.save(warehouse);
    warehouse3 = warehouseRepository.save(warehouse3);
    sampleUser = userRepository.save(sampleUser);

    ItemListEntry itemListEntry = ItemListEntry.builder()
        .item(sampleItem)
        .warehouse(warehouse)
        .quantity(50L)
        .build();
    sampleItem.setEntries(Collections.singletonList(itemListEntry));
    sampleItem = itemRepository.save(sampleItem);
    ItemListEntry savedItemListEntry = itemListEntryRepository.save(itemListEntry);

    OrderRestDTO orderRestDTO = new OrderRestDTO(sampleItem.getId(),
        52L, warehouse.getId(), warehouse3.getId());

    Exception exception = assertThrows(ItemExceedsWarehouseCapacityException.class,
        () -> orderService.createOrder(orderRestDTO));

    String expectedMessage = "2 items missing in warehouse with ID " + warehouse.getId();
    String actualMessage = exception.getMessage();
    System.out.println(actualMessage);
    Assertions.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @MockCustomJwtUser
  void shouldNotCreateOrderNoCapacity() {
    warehouse = warehouseRepository.save(warehouse);
    warehouse3 = warehouseRepository.save(warehouse3);
    sampleUser = userRepository.save(sampleUser);

    ItemListEntry itemListEntry = ItemListEntry.builder()
        .item(sampleItem)
        .warehouse(warehouse)
        .quantity(50L)
        .build();
    sampleItem.setEntries(Collections.singletonList(itemListEntry));
    sampleItem = itemRepository.save(sampleItem);
    ItemListEntry savedItemListEntry = itemListEntryRepository.save(itemListEntry);

    OrderRestDTO orderRestDTO = new OrderRestDTO(sampleItem.getId(),
        48L, warehouse.getId(), warehouse3.getId());

    Exception exception = assertThrows(ItemExceedsWarehouseCapacityException.class,
        () -> orderService.createOrder(orderRestDTO));

    String expectedMessage = "Item exceeds warehouse capacity by " + 400;
    String actualMessage = exception.getMessage();
    Assertions.assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @MockCustomJwtUser
  void createOrderWith0Quantity() {
    warehouse = warehouseRepository.save(warehouse);
    warehouse2 = warehouseRepository.save(warehouse2);
    sampleUser = userRepository.save(sampleUser);

    ItemListEntry itemListEntry = ItemListEntry.builder()
        .item(sampleItem)
        .warehouse(warehouse)
        .quantity(5L)
        .build();
    sampleItem.setEntries(Collections.singletonList(itemListEntry));
    sampleItem = itemRepository.save(sampleItem);
    ItemListEntry savedItemListEntry = itemListEntryRepository.save(itemListEntry);

    OrderRestDTO orderRestDTO = new OrderRestDTO(sampleItem.getId(),
        5L, warehouse.getId(), warehouse2.getId());

    Order createdOrder = orderService.createOrder(orderRestDTO);

    Optional<Order> orderOptional = orderRepository.findById(createdOrder.getId());
    Assertions.assertTrue(orderOptional.isPresent());
    Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getId());
    Optional<Warehouse> warehouse2Optional = warehouseRepository.findById(warehouse2.getId());
    Assertions.assertTrue(warehouse2Optional.isPresent());
    Assertions.assertTrue(warehouseOptional.isPresent());
    warehouse = warehouseOptional.get();
    warehouse2 = warehouse2Optional.get();
    assertThat(warehouse.getEntries().size()).isEqualTo(0);
    assertThat(warehouse2.getEntries().size()).isEqualTo(1);
    List<Order> result = orderService.findAllOrders();

    assertThat(result).isNotEmpty()
        .hasSize(1);
  }

  @Test
  @MockCustomJwtUser
  void createOrderItemInOtherWarehouse() {
    warehouse = warehouseRepository.save(warehouse);
    warehouse2 = warehouseRepository.save(warehouse2);
    sampleUser = userRepository.save(sampleUser);
    Long quantity = 5L;
    ItemListEntry itemListEntry = ItemListEntry.builder()
        .item(sampleItem)
        .warehouse(warehouse)
        .quantity(quantity)
        .build();
    ItemListEntry itemListEntry2 = ItemListEntry.builder()
        .item(sampleItem)
        .warehouse(warehouse2)
        .quantity(quantity)
        .build();

    sampleItem.setEntries(new ArrayList<>(Arrays.asList(itemListEntry, itemListEntry2)));
    sampleItem = itemRepository.save(sampleItem);
    ItemListEntry savedItemListEntry = itemListEntryRepository.save(itemListEntry);
    ItemListEntry savedItemListEntry2 = itemListEntryRepository.save(itemListEntry2);

    OrderRestDTO orderRestDTO = new OrderRestDTO(sampleItem.getId(),
        5L, warehouse.getId(), warehouse2.getId());

    Order createdOrder = orderService.createOrder(orderRestDTO);

    Optional<Order> orderOptional = orderRepository.findById(createdOrder.getId());
    Assertions.assertTrue(orderOptional.isPresent());
    Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getId());
    Optional<Warehouse> warehouse2Optional = warehouseRepository.findById(warehouse2.getId());
    Assertions.assertTrue(warehouse2Optional.isPresent());
    Assertions.assertTrue(warehouseOptional.isPresent());
    warehouse = warehouseOptional.get();
    warehouse2 = warehouse2Optional.get();
    assertThat(warehouse.getEntries().size()).isEqualTo(0);
    assertThat(warehouse2.getEntries().size()).isEqualTo(1);
    assertThat(warehouse2.getEntries().get(0).getQuantity()).isEqualTo(quantity * 2);
    List<Order> result = orderService.findAllOrders();

    assertThat(result).isNotEmpty()
        .hasSize(1);
  }

}
