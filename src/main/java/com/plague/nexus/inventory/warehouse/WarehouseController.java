package com.plague.nexus.inventory.warehouse;

import com.plague.nexus.inventory.dto.WarehouseRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/warehouses")
// Setting up CORS to allow future React/Native frontend development
@CrossOrigin(origins = "*", maxAge = 3600)
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    // POST /api/inventory/warehouses
    @PostMapping
    // Security check: Only ADMIN or WAREHOUSE roles can create a warehouse.
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        Warehouse newWarehouse = warehouseService.createWarehouse(request);
        return new ResponseEntity<>(newWarehouse, HttpStatus.CREATED);
    }

    // GET /api/inventory/warehouses
    @GetMapping
    // Viewing is allowed for Accounting as well
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE') or hasRole('ACCOUNTING')")
    public List<Warehouse> getAllWarehouses() {
        return warehouseService.findAllWarehouses();
    }

    // GET /api/inventory/warehouses/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE') or hasRole('ACCOUNTING')")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.findWarehouseById(id);
        return ResponseEntity.ok(warehouse);
    }

    // PUT /api/inventory/warehouses/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseRequest request) {
        Warehouse updatedWarehouse = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(updatedWarehouse);
    }

    // DELETE /api/inventory/warehouses/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')") // Deleting is a critical, admin-only task
    public void deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
    }
}