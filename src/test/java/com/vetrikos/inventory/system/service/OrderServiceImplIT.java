package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.config.IntegrationTest;
import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.Order;
import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.OrderRestDTO;
import com.vetrikos.inventory.system.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceImplIT {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllOrders() {
        // Arrange
        List<Order> expectedOrders = Collections.singletonList(new Order());
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.findAllOrders();

        // Assert
        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testCreateOrder() {
        // Arrange
        OrderRestDTO requestRestDTO = new OrderRestDTO();
        requestRestDTO.setCreatedBy(UUID.randomUUID());
        requestRestDTO.setSourceId(2L);
        requestRestDTO.setDestinationId(5L);
        requestRestDTO.setItemId(4L);
        requestRestDTO.setQuantity(2L);

        when(userService.findById(requestRestDTO.getCreatedBy())).thenReturn(new User());
        when(itemService.findItemInWarehouse(requestRestDTO.getSourceId(), requestRestDTO.getItemId())).thenReturn(new Item());
        when(warehouseService.findById(requestRestDTO.getSourceId())).thenReturn(new Warehouse());
        when(warehouseService.findById(requestRestDTO.getDestinationId())).thenReturn(new Warehouse());

        // Act
        Order createdOrder = orderService.createOrder(requestRestDTO);
         
        // Assert
        verify(orderRepository, times(1)).save(any(Order.class));
        // Add additional assertions based on your requirements
    }
}