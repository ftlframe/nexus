package com.plague.nexus.user.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Spring Data JPA is smart. It will automatically create a query
    // for us just from the method name "findByName".
    Optional<Role> findByName(RoleType name);
}