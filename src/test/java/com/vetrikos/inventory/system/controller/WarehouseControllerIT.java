package com.vetrikos.inventory.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetrikos.inventory.system.config.CustomPostgreSQLContainer;
import com.vetrikos.inventory.system.config.IntegrationTest;
import com.vetrikos.inventory.system.config.InventoryKeycloakContainer;
import com.vetrikos.inventory.system.config.KeycloakTestUtils;
import com.vetrikos.inventory.system.config.SecurityConfiguration.ConfigAnonUserRoles.Fields;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.mapper.WarehouseMapper;
import com.vetrikos.inventory.system.model.BasicWarehouseRestDTO;
import com.vetrikos.inventory.system.model.FullWarehouseRestDTO;
import com.vetrikos.inventory.system.model.WarehouseRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseUpdateRequestRestDTO;
import com.vetrikos.inventory.system.repository.UserRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.vetrikos.inventory.system.config.Constants.WAREHOUSE_API_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WarehouseControllerIT {

    @Container
    public static CustomPostgreSQLContainer postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

    @Container
    public static InventoryKeycloakContainer keycloakContainer = InventoryKeycloakContainer.getInstance();

    @Autowired
    private KeycloakTestUtils keycloakTestUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private MockMvc mockMvc;

    private User sampleUser;

    private Warehouse sampleWarehouse;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(UUID.randomUUID())
                .username("user")
                .fullName("Sample User")
                .roles(List.of(Fields.ROLE_ADMIN))
                .build();

        sampleWarehouse = Warehouse.builder()
                .name("Test warehouse")
                .capacity(500L)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateWarehouse() throws Exception {
        String warehouseName = "Test warehouse";
        long warehouseCapacity = 500;

//    User user = userRepository.save(sampleUser);

        WarehouseRequestRestDTO requestDTO = new WarehouseRequestRestDTO(warehouseCapacity,
                warehouseName);

        MvcResult mvcResult = mockMvc.perform(post(WAREHOUSE_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .header(HttpHeaders.AUTHORIZATION, keycloakTestUtils.getBearerToken()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        FullWarehouseRestDTO result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), FullWarehouseRestDTO.class);

        Optional<Warehouse> savedWarehouse = warehouseRepository.findById(result.getId());

        // assert saved warehouse response
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCapacity()).isEqualTo(warehouseCapacity);
        assertThat(result.getName()).isEqualTo(warehouseName);
        assertThat(result.getItemsCapacitySize()).isZero();
//    assertThat(result.getUsers()).hasSize(1);

        // assert saved entities
        assertThat(savedWarehouse).isPresent();
//    assertThat(savedWarehouse.get().getUsers()).isNotEmpty()
//        .usingRecursiveFieldByFieldElementComparator().contains(user);
//    assertThat(user.getWarehouse()).usingRecursiveComparison()
//        .isEqualTo(savedWarehouse.get());
    }

    @Test
    void deleteWarehouse() throws Exception {
        sampleUser.setWarehouse(sampleWarehouse);
        User user = userRepository.save(sampleUser);
        Warehouse warehouse = user.getWarehouse();

        mockMvc.perform(delete(WAREHOUSE_API_URL + "/" + warehouse.getId())
                        .header(HttpHeaders.AUTHORIZATION, keycloakTestUtils.getBearerToken()))
                .andExpect(status().isNoContent());

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(warehouse.getId());
        user = userRepository.findById(user.getId()).get();

        assertThat(optionalWarehouse).isEmpty();
        assertThat(user.getWarehouse()).isNull();
    }

    @Test
    @Transactional
    void getWarehouseById() throws Exception {
        sampleUser.setWarehouse(sampleWarehouse);
        User user = userRepository.save(sampleUser);
        Warehouse warehouse = user.getWarehouse();

        mockMvc.perform(get(WAREHOUSE_API_URL + "/" + warehouse.getId())
                        .header(HttpHeaders.AUTHORIZATION, keycloakTestUtils.getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(
                        warehouseMapper.warehouseToFullWarehouseRestDTO(warehouse))));
    }

    @Test
    void listWarehouses() throws Exception {
        sampleUser.setWarehouse(sampleWarehouse);
        User user = userRepository.save(sampleUser);
        List<BasicWarehouseRestDTO> warehouses = Stream.of(user.getWarehouse())
                .map(warehouseMapper::warehouseToBasicWarehouseRestDTO).toList();

        mockMvc.perform(get(WAREHOUSE_API_URL)
                        .header(HttpHeaders.AUTHORIZATION, keycloakTestUtils.getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(warehouses)));
    }

    @Test
    void updateWarehouse() throws Exception {
        sampleUser.setWarehouse(sampleWarehouse);

        String newName = "New name";
        long newCapacity = 1500;

        User user = userRepository.save(sampleUser);
        Warehouse expectedWarehouse = user.getWarehouse();
        expectedWarehouse.setName(newName);
        expectedWarehouse.setCapacity(newCapacity);
        expectedWarehouse.setUsers(List.of(user));

        WarehouseUpdateRequestRestDTO requestDTO = new WarehouseUpdateRequestRestDTO();
        requestDTO.setName(newName);
        requestDTO.setCapacity(newCapacity);

        mockMvc.perform(put(WAREHOUSE_API_URL + "/" + expectedWarehouse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .header(HttpHeaders.AUTHORIZATION, keycloakTestUtils.getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(
                        warehouseMapper.warehouseToFullWarehouseRestDTO(expectedWarehouse))));
    }
}
