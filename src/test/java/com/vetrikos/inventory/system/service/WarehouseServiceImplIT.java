package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.config.CustomPostgreSQLContainer;
import com.vetrikos.inventory.system.config.IntegrationTest;
import com.vetrikos.inventory.system.config.InventoryKeycloakContainer;
import com.vetrikos.inventory.system.entity.BasicWarehouse;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.mapper.WarehouseMapper;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseUpdateRequestRestDTO;
import com.vetrikos.inventory.system.repository.UserRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WarehouseServiceImplIT {

    @Container
    public static CustomPostgreSQLContainer postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

    @Container
    public static InventoryKeycloakContainer keycloakContainer = InventoryKeycloakContainer.getInstance();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseServiceImpl warehouseService;

    @Autowired
    private WarehouseMapper warehouseMapper;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(UUID.randomUUID())
                .username("user")
                .fullName("Sample User")
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    void shouldFindAll() {
        Warehouse warehouse = Warehouse.builder()
                .capacity(100L)
                .name("Warehouse")
                .users(List.of(sampleUser))
                .build();

        sampleUser.setWarehouse(warehouse);
        sampleUser = userRepository.save(sampleUser);

        List<BasicWarehouse> result = warehouseService.findAll();

        System.out.println(result);
        System.out.println(warehouse.toString());

        assertThat(result).isNotEmpty()
                .hasSize(1);
        assertThat(warehouseMapper.basicWarehouseToBasicWarehouseRestDTO(result.get(0))).isEqualTo(
                warehouseMapper.warehouseToBasicWarehouseRestDTO(warehouse));
    }

    @Test
    void shouldFindById() {
        Warehouse warehouse = Warehouse.builder()
                .capacity(100L)
                .name("Warehouse")
                .users(List.of(sampleUser))
                .build();

        sampleUser.setWarehouse(warehouse);
        sampleUser = userRepository.save(sampleUser);

        Warehouse result = warehouseService.findById(warehouse.getId());

        assertThat(result).isNotNull()
                .isEqualTo(warehouse);
    }

    @Test
    void findByIdShouldThrowException() {
        Long warehouseId = 1L;
        assertThatThrownBy(() -> warehouseService.findById(warehouseId))
                .hasMessage(WarehouseService.warehouseNotFoundMessage(warehouseId));
    }

    @Test
    void shouldCreateWarehouse() {
        sampleUser = userRepository.save(sampleUser);

        long capacity = 100;
        String warehouseName = "Lidl warehouse";
        WarehouseRequestRestDTO requestRestDTO = new WarehouseRequestRestDTO(capacity, warehouseName);

        Warehouse result = warehouseService.createWarehouse(requestRestDTO);

        Warehouse expectedWarehouse = Warehouse.builder()
                .id(result.getId())
                .capacity(capacity)
                .name(warehouseName)
//        .users(List.of(sampleUser))
                .build();

        assertThat(result).isNotNull()
                .isEqualTo(expectedWarehouse);
        assertThat(warehouseRepository.findAll().size()).isOne();
    }

    @Test
    void shouldUpdateWarehouse() {
        Warehouse warehouse = Warehouse.builder()
                .capacity(100L)
                .name("Warehouse")
                .users(List.of(sampleUser))
                .build();

        sampleUser.setWarehouse(warehouse);
        sampleUser = userRepository.save(sampleUser);

        Long warehouseId = warehouse.getId();
        long newCapacity = 200;
        String newWarehouseName = "Lidl warehouse";
        WarehouseUpdateRequestRestDTO requestRestDTO = new WarehouseUpdateRequestRestDTO();
        requestRestDTO.setCapacity(newCapacity);
        requestRestDTO.setName(newWarehouseName);

        Warehouse result = warehouseService.updateWarehouse(warehouseId, requestRestDTO);

        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("capacity", newCapacity)
                .hasFieldOrPropertyWithValue("name", newWarehouseName);
    }

    @Test
    void updateWarehouseShouldThrowException() {
        Long warehouseId = 1L;
        WarehouseUpdateRequestRestDTO requestRestDTO = new WarehouseUpdateRequestRestDTO();
        assertThatThrownBy(() -> warehouseService.updateWarehouse(warehouseId, requestRestDTO))
                .hasMessage(WarehouseService.warehouseNotFoundMessage(warehouseId));
    }

    @Test
    void shouldDeleteWarehouse() {
        Warehouse warehouse = Warehouse.builder()
                .capacity(100L)
                .name("Warehouse")
                .users(List.of(sampleUser))
                .build();

        sampleUser.setWarehouse(warehouse);
        sampleUser = userRepository.save(sampleUser);

        warehouseService.deleteWarehouse(warehouse.getId());

        assertThat(warehouseRepository.findAll()).isEmpty();
        assertThat(userRepository.findAll()).contains(sampleUser);
    }

    @Test
    void deleteWarehouseShouldThrowException() {
        Long warehouseId = 1L;
        assertThatThrownBy(() -> warehouseService.deleteWarehouse(warehouseId))
                .hasMessage(WarehouseService.warehouseNotFoundMessage(warehouseId));
    }
}
