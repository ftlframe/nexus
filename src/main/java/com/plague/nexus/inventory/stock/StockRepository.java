package com.plague.nexus.inventory.stock;
import com.plague.nexus.inventory.warehouse.Warehouse;
import com.plague.nexus.inventory.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Find the stock entry for a specific product in a specific warehouse
    Optional<Stock> findByProductAndWarehouse(Product product, Warehouse warehouse);

    // Find all stock entries for a specific product across all warehouses
    List<Stock> findByProductId(Long productId);
}