package com.vetrikos.inventory.system.config;

import static com.vetrikos.inventory.system.config.SecurityConfiguration.GRANTED_AUTHORITY_ROLE_PREFIX;
import static com.vetrikos.inventory.system.config.SecurityConfiguration.JWT_FULL_NAME_CLAIM_NAME;
import static com.vetrikos.inventory.system.config.SecurityConfiguration.JWT_PREFERRED_USERNAME_CLAIM_NAME;
import static com.vetrikos.inventory.system.config.SecurityConfiguration.JWT_ROLES_CLAIM_NAME;
import static com.vetrikos.inventory.system.config.SecurityConfiguration.USER_UUID_CLAIM_NAME;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
final class TestUserDetailsService implements WithSecurityContextFactory<MockCustomJwtUser> {

  @Override
  public SecurityContext createSecurityContext(MockCustomJwtUser annotation) {
    return getSecurityContext(annotation);
  }


  private SecurityContext getSecurityContext(MockCustomJwtUser annotation) {
    Authentication authentication = getAuthentication(annotation);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    return context;
  }

  private Authentication getAuthentication(MockCustomJwtUser annotation) {
    Jwt jwt = Jwt.withTokenValue("test")
        .header("typ", "JWT")
        .issuedAt(null)
        .subject(annotation.uuid())
        .expiresAt(null)
        .claims(stringObjectMap -> stringObjectMap.putAll(
            Map.ofEntries(Map.entry(JWT_PREFERRED_USERNAME_CLAIM_NAME, annotation.username()),
                Map.entry(JWT_FULL_NAME_CLAIM_NAME, annotation.fullName()),
                Map.entry(USER_UUID_CLAIM_NAME, annotation.uuid()),
                Map.entry(JWT_ROLES_CLAIM_NAME,
                    Arrays.stream(annotation.roles())
                        .map(s -> s.replaceFirst(GRANTED_AUTHORITY_ROLE_PREFIX, ""))
                        .collect(Collectors.toList())))))
        .build();
    return new JwtAuthenticationToken(jwt);
  }
}

