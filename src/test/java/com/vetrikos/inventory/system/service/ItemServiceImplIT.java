package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.config.CustomPostgreSQLContainer;
import com.vetrikos.inventory.system.config.IntegrationTest;
import com.vetrikos.inventory.system.config.InventoryKeycloakContainer;
import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.ItemRepository;
import com.vetrikos.inventory.system.repository.UserRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testcontainers.junit.jupiter.Container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;

@IntegrationTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemServiceImplIT {

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
    private WarehouseServiceImpl warehouseService;

    @Autowired
    private ItemServiceImpl itemService;

    private Item sampleItem;
    private User sampleUser;
    private Warehouse warehouse;
    private Warehouse warehouse2;

    @BeforeEach
    void setUp() {
        sampleItem = Item.builder()
                .name("item1")
                .size(50L)
                .entries(new ArrayList<>())
                .build();
        sampleUser = User.builder()
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
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemListEntryRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    void shouldFindAll() {
        warehouse = warehouseRepository.save(warehouse);

        ItemListEntry itemListEntry = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(10L)
                .build();
        sampleItem.setEntries(Collections.singletonList(itemListEntry));

        Item savedItem = itemRepository.save(sampleItem);
        ItemListEntry savedItemListEntry = itemListEntryRepository.save(itemListEntry);

        List<Item> result = itemService.findAll();

        assertThat(result).isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(savedItem);
    }

    @Test
    void shouldFindById() {
        sampleItem = itemRepository.save(sampleItem);
        Item result = itemService.findById(sampleItem.getId());
        assertThat(result).isNotNull()
                .isEqualTo(sampleItem);
    }

    @Test
    void shouldFindItemsInWarehouse() {

        warehouse = warehouseRepository.save(warehouse);
        Item item2 = Item.builder().name("item2").size(50L).build();

        ItemListEntry itemListEntry1 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(10L)
                .build();

        ItemListEntry itemListEntry2 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(20L)
                .build();
        ItemListEntry itemListEntry3 = ItemListEntry.builder()
                .item(item2)
                .warehouse(warehouse)
                .quantity(30L)
                .build();

        sampleItem.getEntries().add(itemListEntry1);
        sampleItem.getEntries().add(itemListEntry2);
        item2.getEntries().add(itemListEntry3);
        warehouse.getEntries().add(itemListEntry1);
        warehouse.getEntries().add(itemListEntry2);
        warehouse.getEntries().add(itemListEntry3);

        Item savedItem = itemRepository.save(sampleItem);
        Item savedItem2 = itemRepository.save(item2);
        ItemListEntry savedItemListEntry1 = itemListEntryRepository.save(itemListEntry1);
        ItemListEntry savedItemListEntry2 = itemListEntryRepository.save(itemListEntry2);
        ItemListEntry savedItemListEntry3 = itemListEntryRepository.save(itemListEntry3);
        Warehouse savedWarehouseWithEntries = warehouseRepository.save(warehouse);

        List<Item> result = itemService.findItemsInWarehouse(warehouse.getId());
        assertThat(result).isNotNull().isNotEmpty()
                .containsAnyElementsOf(new ArrayList<>(List.of(savedItem, savedItem2)));
    }

    @Test
    void shouldCreateItemInWarehouse() {
        warehouse = warehouseRepository.save(warehouse);
        WarehouseItemRequestRestDTO warehouseItemRestDTO = new WarehouseItemRequestRestDTO();
        String name = "item";
        warehouseItemRestDTO.setName(name);
        warehouseItemRestDTO.setQuantity(50L);
        warehouseItemRestDTO.setSize(50L);
        Item result = itemService.createItem(warehouse.getId(), warehouseItemRestDTO);
        Item item = itemService.findById(result.getId());
        assertThat(result).isEqualTo(item);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getSize()).isEqualTo(50);
        assertThat(result.getEntries().get(0).getQuantity()).isEqualTo(50);
        assertThat(result.getEntries().get(0).getWarehouse()).isEqualTo(warehouse);
    }

    @Test
    void shouldUpdateItemInWarehouse() {
        warehouse = warehouseRepository.save(warehouse);
        WarehouseItemRequestRestDTO warehouseItemRestDTO = new WarehouseItemRequestRestDTO();
        String name = "item";
        warehouseItemRestDTO.setName(name);
        warehouseItemRestDTO.setQuantity(50L);
        warehouseItemRestDTO.setSize(50L);
        Item createdItem = itemService.createItem(warehouse.getId(), warehouseItemRestDTO);
        String newName = "itemNew";
        Long newQuantity = 10L;
        Long newSize = 20L;
        WarehouseItemRequestRestDTO warehouseItemRequestRestDTO = new WarehouseItemRequestRestDTO(
                newName, newSize, newQuantity);

        Item updatedItem = itemService.updateItem(createdItem.getId(), warehouse.getId(),
                warehouseItemRequestRestDTO);
        Item item = itemService.findById(createdItem.getId());

        assertThat(updatedItem).isNotNull().isEqualTo(item);
        assertThat(updatedItem.getSize()).isEqualTo(newSize);
        assertThat(updatedItem.getName()).isEqualTo(newName);
    }


    @Test
    void shouldDeleteItemInWarehouse() {
        warehouse = warehouseRepository.save(warehouse);
        String name = "item";
        WarehouseItemRequestRestDTO warehouseItemRestDTO = new WarehouseItemRequestRestDTO(name, 50L,
                50L);

        Item createdItem = itemService.createItem(warehouse.getId(), warehouseItemRestDTO);

        itemService.deleteItem(createdItem.getId());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.findById(createdItem.getId());
        });

        String expectedMessage = "Item with id " + createdItem.getId() + " not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void shouldDeleteItemEntriesInWarehouse() {
        warehouse = warehouseRepository.save(warehouse);
        Item item2 = Item.builder().name("item2").size(50L).build();

        ItemListEntry itemListEntry1 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(10L)
                .build();

        ItemListEntry itemListEntry2 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(20L)
                .build();
        ItemListEntry itemListEntry3 = ItemListEntry.builder()
                .item(item2)
                .warehouse(warehouse)
                .quantity(30L)
                .build();

        sampleItem.getEntries().add(itemListEntry1);
        sampleItem.getEntries().add(itemListEntry2);
        item2.getEntries().add(itemListEntry3);
        warehouse.getEntries().add(itemListEntry1);
        warehouse.getEntries().add(itemListEntry2);
        warehouse.getEntries().add(itemListEntry3);

        Item savedItem = itemRepository.save(sampleItem);
        Item savedItem2 = itemRepository.save(item2);
        ItemListEntry savedItemListEntry1 = itemListEntryRepository.save(itemListEntry1);
        ItemListEntry savedItemListEntry2 = itemListEntryRepository.save(itemListEntry2);
        ItemListEntry savedItemListEntry3 = itemListEntryRepository.save(itemListEntry3);
        Warehouse savedWarehouseWithEntries = warehouseRepository.save(warehouse);

        itemService.deleteWarehouseItem(savedItem.getId(), warehouse.getId());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.findItemInWarehouse(warehouse.getId(), savedItem.getId());
        });
        Warehouse warehouseAfterDelete = warehouseService.findById(warehouse.getId());
        String expectedMessage = ItemListEntryService.itemInWarehouseNotFoundMessage(savedItem.getId(),
                warehouse.getId());
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
        assertThat(itemService.findById(savedItem.getId())).isNotNull();
        assertThat(warehouseAfterDelete.getEntries()).containsExactlyInAnyOrder(itemListEntry3);
    }

    @Test
    void shouldFindItemInWarehouse() {
        warehouse = warehouseRepository.save(warehouse);
        Item item2 = Item.builder().name("item2").size(50L).build();

        ItemListEntry itemListEntry1 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(10L)
                .build();

        ItemListEntry itemListEntry2 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(20L)
                .build();
        ItemListEntry itemListEntry3 = ItemListEntry.builder()
                .item(item2)
                .warehouse(warehouse)
                .quantity(30L)
                .build();

        sampleItem.getEntries().add(itemListEntry1);
        sampleItem.getEntries().add(itemListEntry2);
        item2.getEntries().add(itemListEntry3);
        warehouse.getEntries().add(itemListEntry1);
        warehouse.getEntries().add(itemListEntry2);
        warehouse.getEntries().add(itemListEntry3);

        Item savedItem = itemRepository.save(sampleItem);
        Item savedItem2 = itemRepository.save(item2);
        ItemListEntry savedItemListEntry1 = itemListEntryRepository.save(itemListEntry1);
        ItemListEntry savedItemListEntry2 = itemListEntryRepository.save(itemListEntry2);
        ItemListEntry savedItemListEntry3 = itemListEntryRepository.save(itemListEntry3);
        Warehouse savedWarehouseWithEntries = warehouseRepository.save(warehouse);

        Item result = itemService.findItemInWarehouse(warehouse.getId(), savedItem.getId());
        assertThat(result).isNotNull().isEqualTo(savedItem);
    }

    @Test
    void shouldNotFindItemInWarehouse() {
        warehouse = warehouseRepository.save(warehouse);
        Item item2 = Item.builder().name("item2").size(50L).build();

        ItemListEntry itemListEntry1 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(10L)
                .build();

        ItemListEntry itemListEntry2 = ItemListEntry.builder()
                .item(sampleItem)
                .warehouse(warehouse)
                .quantity(20L)
                .build();
        ItemListEntry itemListEntry3 = ItemListEntry.builder()
                .item(item2)
                .warehouse(warehouse)
                .quantity(30L)
                .build();

        sampleItem.getEntries().add(itemListEntry1);
        sampleItem.getEntries().add(itemListEntry2);
        item2.getEntries().add(itemListEntry3);
        warehouse.getEntries().add(itemListEntry1);
        warehouse.getEntries().add(itemListEntry2);
        warehouse.getEntries().add(itemListEntry3);

        Item savedItem = itemRepository.save(sampleItem);
        Item savedItem2 = itemRepository.save(item2);
        ItemListEntry savedItemListEntry1 = itemListEntryRepository.save(itemListEntry1);
        ItemListEntry savedItemListEntry2 = itemListEntryRepository.save(itemListEntry2);
        ItemListEntry savedItemListEntry3 = itemListEntryRepository.save(itemListEntry3);
        Warehouse savedWarehouseWithEntries = warehouseRepository.save(warehouse);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.findItemInWarehouse(warehouse.getId(), 50L);
        });

        String expectedMessage = "Item with id " + 50 + " not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateItemShouldThrowException() {
        warehouse = warehouseRepository.save(warehouse);

        WarehouseItemRequestRestDTO warehouseItemRestDTO = new WarehouseItemRequestRestDTO();
        String name = "item";
        warehouseItemRestDTO.setName(name);
        warehouseItemRestDTO.setQuantity(50L);
        warehouseItemRestDTO.setSize(50L);
        Item createdItem = itemService.createItem(warehouse.getId(), warehouseItemRestDTO);
        Long capacity = warehouse.getCapacity();
        Long currentCapacity = itemService.calculateCurrentCapacity(warehouse);

        String newName = "itemNew";
        Long newQuantity = 29L;
        Long newSize = 100L;
        Long newItemCapacity = newQuantity * newSize;
        WarehouseItemRequestRestDTO warehouseItemRequestRestDTO = new WarehouseItemRequestRestDTO(
                newName, newSize, newQuantity);

        assertThatThrownBy(() -> itemService.updateItem(createdItem.getId(), warehouse.getId(),
                warehouseItemRequestRestDTO))
                .hasMessage(
                        ItemService.itemExceedsCapacityMessage(currentCapacity + newItemCapacity - capacity));
    }

    @Test
    void createItemInWarehouseShouldThrowException() {
        warehouse = warehouseRepository.save(warehouse);
        WarehouseItemRequestRestDTO warehouseItemRestDTO = new WarehouseItemRequestRestDTO();
        String name = "item";
        warehouseItemRestDTO.setName(name);
        warehouseItemRestDTO.setQuantity(58L);
        warehouseItemRestDTO.setSize(50L);
        assertThatThrownBy(() -> itemService.createItem(warehouse.getId(), warehouseItemRestDTO))
                .hasMessage(ItemService.itemExceedsCapacityMessage(100L));
    }
}
