package com.vetrikos.inventory.system.service;


import com.vetrikos.inventory.system.entity.Order;
import com.vetrikos.inventory.system.model.OrderRestDTO;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.lang.NonNull;

public interface OrderService {

    @NonNull
    @lombok.NonNull
    List<Order> findAllOrders();

    @NonNull
    Order createOrder(@NotNull OrderRestDTO requestRestDTO);
}
