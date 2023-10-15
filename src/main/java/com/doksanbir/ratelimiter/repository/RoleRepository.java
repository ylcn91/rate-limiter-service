package com.doksanbir.ratelimiter.repository;

import com.doksanbir.ratelimiter.model.Role;
import com.doksanbir.ratelimiter.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
