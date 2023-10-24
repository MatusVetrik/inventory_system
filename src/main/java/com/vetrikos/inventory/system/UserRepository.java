package com.vetrikos.inventory.system;

import com.vetrikos.inventory.system.classes.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
