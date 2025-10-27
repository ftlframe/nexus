package com.plague.nexus.inventory.warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // ADD THIS IMPORT

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    // New derived query for checking uniqueness
    Optional<Warehouse> findByName(String name);
}