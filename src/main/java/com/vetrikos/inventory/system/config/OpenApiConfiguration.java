package com.vetrikos.inventory.system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Inventory API"))
public class OpenApiConfiguration {

  @Value("${OAUTH2_ISSUER_URI}")
  private String oauth2IssuerUri;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(new Components().addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            ).addSecuritySchemes("oauth2",
                new SecurityScheme()
                    .type(Type.OAUTH2)
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl(oauth2IssuerUri + "/protocol/openid-connect/auth")
                            .tokenUrl(oauth2IssuerUri + "/protocol/openid-connect/token")))
                    .bearerFormat("JWT")
            )
        );
  }

  @Bean
  public OpenApiCustomizer securityItemCustomiser() {
    return openApi -> openApi.getPaths().values().stream()
        .flatMap(pathItem -> pathItem.readOperations().stream())
        .forEach(operation -> operation
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .addSecurityItem(new SecurityRequirement().addList("oauth2")));
  }
}
