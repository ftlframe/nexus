package com.plague.nexus.inventory.warehouse;

import com.plague.nexus.inventory.dto.WarehouseRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Transactional
    public Warehouse createWarehouse(WarehouseRequest request) {
        // Simple business rule: Check for duplicate name (optional, but good practice)
        if (warehouseRepository.findByName(request.name()).isPresent()) {
            throw new IllegalArgumentException("A warehouse with the name " + request.name() + " already exists.");
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setName(request.name());
        warehouse.setLocationAddress(request.locationAddress());
        return warehouseRepository.save(warehouse);
    }

    @Transactional(readOnly = true)
    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + id));
    }

    @Transactional
    public Warehouse updateWarehouse(Long id, WarehouseRequest request) {
        Warehouse warehouse = findWarehouseById(id);
        warehouse.setName(request.name());
        warehouse.setLocationAddress(request.locationAddress());
        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public void deleteWarehouse(Long id) {
        // In a real app, this should check for existing stock before deleting.
        if (!warehouseRepository.existsById(id)) {
            throw new EntityNotFoundException("Warehouse not found with ID: " + id);
        }
        warehouseRepository.deleteById(id);
    }
}