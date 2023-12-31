package com.vetrikos.inventory.system.config;


import static com.vetrikos.inventory.system.config.Constants.ADMIN_PASSWORD;
import static com.vetrikos.inventory.system.config.Constants.ADMIN_USERNAME;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Component;

@Component
public class KeycloakTestUtils {

  private static final String REALM_NAME = "inventory_system";
  private static final String CLIENT_ID = "frontend";

  public String getBearerToken() {
    return getBearerToken(ADMIN_USERNAME, ADMIN_PASSWORD);
  }

  public String getBearerToken(String username, String password) {
      return "Bearer " + getAccessToken(username, password).getToken();
  }

  public AccessTokenResponse getAccessToken(String username, String password) {
    try (Keycloak keycloak = KeycloakBuilder.builder()
        .serverUrl(InventoryKeycloakContainer.getInstance().getAuthServerUrl())
        .realm(REALM_NAME)
        .clientId(CLIENT_ID)
        .username(username)
        .password(password)
        .build()) {
      return keycloak.tokenManager().getAccessToken();
    }
  }
}
