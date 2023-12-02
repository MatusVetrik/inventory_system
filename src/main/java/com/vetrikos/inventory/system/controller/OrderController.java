package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.OrdersApi;
import com.vetrikos.inventory.system.config.SecurityConfiguration.ConfigAnonUserRoles.Fields;
import com.vetrikos.inventory.system.mapper.OrderMapper;
import com.vetrikos.inventory.system.model.OrderResponseRestDTO;
import com.vetrikos.inventory.system.model.OrderRestDTO;
import com.vetrikos.inventory.system.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    @PreAuthorize("hasAnyRole('" + Fields.ROLE_ADMIN + "','" + Fields.ROLE_MANAGER + "')")
    public ResponseEntity<OrderResponseRestDTO> createOrder(OrderRestDTO orderRestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.orderToOrderResponseDTO(
                        orderService.createOrder(orderRestDTO)));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponseRestDTO>> listOrders() {
        return ResponseEntity.ok(orderService.findAllOrders().stream()
                .map(orderMapper::orderToOrderResponseDTO)
                .toList());
    }
}
