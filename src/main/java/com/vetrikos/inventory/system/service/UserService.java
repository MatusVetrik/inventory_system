package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

public interface UserService {

    @NonNull
    User findById(@NotNull UUID userId);

    @NonNull
    User findByWarehouseAndUserId(@NotNull Long warehouseId, @NotNull UUID userId);

    Boolean existsById(@NotNull UUID userId);

    @NonNull
    List<User> findAll();

    @NonNull
    List<User> findAllByWarehouseId(@NotNull Long warehouseId);

    @NonNull
    User createUser(@NotNull User userRequest);

    @NonNull
    User updateUser(@NotNull UUID userId, @NotNull User userRequest);

    void deleteUser(@NotNull UUID userId);

}


