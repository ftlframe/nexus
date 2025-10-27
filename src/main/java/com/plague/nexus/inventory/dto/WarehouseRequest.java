package com.plague.nexus.inventory.dto;

import jakarta.validation.constraints.NotBlank;

// Record is perfect for simple request objects, using validation annotations
public record WarehouseRequest(
        @NotBlank(message = "Warehouse name is required.")
        String name,

        @NotBlank(message = "Warehouse address is required.")
        String locationAddress
) {}