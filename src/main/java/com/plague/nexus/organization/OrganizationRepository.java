// File: src/main/java/com/plague/nexus/organization/OrganizationRepository.java

package com.plague.nexus.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // By extending JpaRepository, we get all these methods for free:
    // - save(Organization org)
    // - findById(Long id)
    // - findAll()
    // - delete(Organization org)
    // ...and many more!

    Optional<Organization> findByName(String name);
}