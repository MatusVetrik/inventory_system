package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.WarehouseUsersApi;
import com.vetrikos.inventory.system.config.SecurityConfiguration.ConfigAnonUserRoles.Fields;
import com.vetrikos.inventory.system.mapper.UserMapper;
import com.vetrikos.inventory.system.model.UserRestDTO;
import com.vetrikos.inventory.system.service.UserService;
import com.vetrikos.inventory.system.service.WarehouseService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WarehouseUsersController implements WarehouseUsersApi {

  private final UserService userService;
  private final WarehouseService warehouseService;
  private final UserMapper userMapper;

  @Override
  @PreAuthorize("hasAnyRole('" + Fields.ROLE_ADMIN + "','" + Fields.ROLE_MANAGER + "')")
  public ResponseEntity<Void> addWarehouseUser(Long warehouseId, UUID userId) {
    warehouseService.addUserToWarehouse(warehouseId, userId);
    return ResponseEntity.ok().build();
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserRestDTO> getWarehouseUserById(Long warehouseId, UUID userId) {
    return ResponseEntity.ok(
        userMapper.userToUserRestDTO(userService.findByWarehouseAndUserId(warehouseId, userId)));
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<UserRestDTO>> getWarehouseUsers(Long warehouseId) {
    return ResponseEntity.ok(
        userService.findAllByWarehouseId(warehouseId).stream().map(userMapper::userToUserRestDTO)
            .toList());
  }

  @Override
  @PreAuthorize("hasAnyRole('" + Fields.ROLE_ADMIN + "','" + Fields.ROLE_MANAGER + "')")
  public ResponseEntity<Void> removeWarehouseUser(Long warehouseId, UUID userId) {
    warehouseService.deleteUserFromWarehouse(warehouseId, userId);
    return ResponseEntity.ok().build();
  }
}
