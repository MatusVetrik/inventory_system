package com.vetrikos.inventory.system.config;


import dasniko.testcontainers.keycloak.ExtendableKeycloakContainer;

public class InventoryKeycloakContainer extends ExtendableKeycloakContainer<InventoryKeycloakContainer> {

  private static final String DOCKER_IMAGE_NAME = "quay.io/keycloak/keycloak:20.0.3";
  private static final String KEYCLOAK_REALM_EXPORT_PATH = "keycloak/realm_export.json";
  private static final String KEYCLOAK_REALM_NAME = "master";

  private static InventoryKeycloakContainer container;

  private InventoryKeycloakContainer() {
    super(DOCKER_IMAGE_NAME);
    super.withRealmImportFile(KEYCLOAK_REALM_EXPORT_PATH);
  }

  public static InventoryKeycloakContainer getInstance() {
    if (container == null) {
      container = new InventoryKeycloakContainer();
    }
    return container;
  }

  public String getKeycloakIssuerURI() {
    return container.getAuthServerUrl() + "realms/" + KEYCLOAK_REALM_NAME;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("OAUTH2_ISSUER_URI", getKeycloakIssuerURI());
  }

  @Override
  public void stop() {
  }
}

