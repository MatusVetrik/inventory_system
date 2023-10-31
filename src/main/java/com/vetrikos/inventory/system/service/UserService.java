package com.vetrikos.inventory.system.service;

import java.util.UUID;

public interface UserService {

  String USER_NOT_FOUND_STRING_FORMAT = "User with id %s not found";

  static String userNotFoundMessage(UUID userId) {
    return String.format(USER_NOT_FOUND_STRING_FORMAT, userId.toString());
  }

}
