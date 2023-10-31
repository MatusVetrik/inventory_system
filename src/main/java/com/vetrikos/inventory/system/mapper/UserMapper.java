package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.model.UserRestDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

  UserRestDTO userToUserRestDTO(User user);
}
