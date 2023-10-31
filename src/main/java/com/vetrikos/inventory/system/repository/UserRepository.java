package com.vetrikos.inventory.system.repository;

import com.vetrikos.inventory.system.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
}
