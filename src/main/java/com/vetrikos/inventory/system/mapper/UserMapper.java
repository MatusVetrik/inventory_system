package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.model.UserRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.UUID;

@Mapper
public interface UserMapper {

    @Mapping(target = "warehouseName", source = "warehouse.name", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    UserRestDTO userToUserRestDTO(User user);

    @Named("userToUuid")
    default UUID userToUuid(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("uuidToUser")
    default User uuidToUser(UUID userId) {
        User user = new User();
        user.setId(userId);
        return user;
    }
}
