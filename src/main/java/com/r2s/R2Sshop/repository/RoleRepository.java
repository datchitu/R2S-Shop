package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByRoleName(String name);

    Boolean existsByRoleName(String roleName);
}
