package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.model.UserRestDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String USER_NOT_FOUND_STRING_FORMAT = "User with id %s not found";

    static String userNotFoundMessage(UUID userId) {
        return String.format(USER_NOT_FOUND_STRING_FORMAT, userId.toString());
    }

    @NonNull
    User findById(@NotNull UUID userId);

    @NonNull
    List<User> findAll();

    @NonNull
    User createUser(@NotNull UserRestDTO requestRestDTO);

    @NonNull
    User updateUser(@NotNull UUID userId, @NotNull UserRestDTO updateRequestRestDTO);

    void deleteUser(@NotNull UUID userId);

}


