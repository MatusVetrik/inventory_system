package com.vetrikos.inventory.system.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@WithSecurityContext(factory = TestUserDetailsService.class)
public @interface MockCustomJwtUser {

  String username() default "user";

  String[] roles() default {SecurityConfiguration.ConfigAnonUserRoles.Fields.ROLE_USER};

  String fullName() default "John Doe";

  String uuid() default "70c42715-ebd5-44bb-b1de-b74f44c6d110";

}
