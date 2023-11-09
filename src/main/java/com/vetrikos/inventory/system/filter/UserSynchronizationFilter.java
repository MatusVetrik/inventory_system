package com.vetrikos.inventory.system.filter;

import static com.vetrikos.inventory.system.config.SecurityConfiguration.JWT_FULL_NAME_CLAIM_NAME;
import static com.vetrikos.inventory.system.config.SecurityConfiguration.parseClaimAsStringOrElse;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizationFilter extends OncePerRequestFilter {

  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("UserRegistrationFilter called for {} request with path {}", request.getMethod(),
        request.getServletPath());

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt principal = (Jwt) authentication.getPrincipal();

    UUID userId = UUID.fromString(principal.getSubject());

    User user = User.builder()
        .id(userId)
        .username(authentication.getName())
        .fullName(
            parseClaimAsStringOrElse(principal, JWT_FULL_NAME_CLAIM_NAME, authentication.getName()))
        .roles(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
            Collectors.toList()))
        .build();

    if (userService.existsById(userId)) {
      log.info("Updating user data for user with id {}", userId);
      userService.updateUser(userId, user);
    } else {
      log.info("Creating user {}", user);
      userService.createUser(user);
    }

    filterChain.doFilter(request, response);
  }
}
