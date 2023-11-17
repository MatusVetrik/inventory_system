package com.vetrikos.inventory.system.controller;

import com.vetrikos.inventory.system.api.UsersApi;
import com.vetrikos.inventory.system.config.SecurityConfiguration.ConfigAnonUserRoles.Fields;
import com.vetrikos.inventory.system.mapper.UserMapper;
import com.vetrikos.inventory.system.model.UserRestDTO;
import com.vetrikos.inventory.system.service.UserService;
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
public class UserController implements UsersApi {

  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  @PreAuthorize("hasAnyRole('" + Fields.ROLE_ADMIN + "','" + Fields.ROLE_MANAGER + "')")
  public ResponseEntity<List<UserRestDTO>> listUsers() {
    return ResponseEntity.ok(
        userService.findAll().stream().map(userMapper::userToUserRestDTO).toList());
  }

  @Override
  @PreAuthorize("hasAnyRole('" + Fields.ROLE_ADMIN + "','" + Fields.ROLE_MANAGER + "')")
  public ResponseEntity<UserRestDTO> getUserById(UUID userId) {
    return ResponseEntity.ok(userMapper.userToUserRestDTO(userService.findById(userId)));
  }
}
