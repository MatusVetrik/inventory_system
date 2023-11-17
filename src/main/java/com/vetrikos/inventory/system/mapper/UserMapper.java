package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.model.UserRestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper
public interface UserMapper {

  @Mapping(target = "warehouseName", source = "warehouse.name", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  UserRestDTO userToUserRestDTO(User user);
}
