package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.OrdersApi;
import com.vetrikos.inventory.system.config.SecurityConfiguration;
import com.vetrikos.inventory.system.mapper.OrderMapper;
import com.vetrikos.inventory.system.model.OrderRestDTO;
import com.vetrikos.inventory.system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    @PreAuthorize("hasRole('" + SecurityConfiguration.ConfigAnonUserRoles.Fields.ROLE_ADMIN + "')")
    public ResponseEntity<OrderRestDTO> createOrder(OrderRestDTO orderRestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.orderToOrderDTO(
                        orderService.createOrder(orderRestDTO)));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderRestDTO>> listOrders() {
        return ResponseEntity.ok(orderService.findAllOrders().stream()
                .map(orderMapper::orderToOrderDTO)
                .toList());
    }
}
