package com.vetrikos.inventory.system.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.UserRestDTO;
import com.vetrikos.inventory.system.config.SecurityConfiguration.ConfigAnonUserRoles.Fields;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    UserMapperImpl.class,
})
class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  void userToUserRestDTO() {
    UUID id = UUID.randomUUID();
    String username = "useros";
    String warehouseName = "Sample warehouse";
    String fullName = "Sample User";
    List<String> roles = List.of(Fields.ROLE_USER);
    User user = User.builder()
        .id(id)
        .username(username)
        .fullName(fullName)
        .warehouse(Warehouse.builder()
            .name(warehouseName)
            .build())
        .roles(roles)
        .build();
    UserRestDTO expectedResult = new UserRestDTO(id, username, fullName, roles);
    expectedResult.setWarehouseName(warehouseName);

    UserRestDTO result = userMapper.userToUserRestDTO(user);

    assertThat(result).isNotNull()
        .isEqualTo(expectedResult);
  }
}