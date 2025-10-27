package com.plague.nexus.user.role;

/**
 * Defines the types of roles available in the system.
 */
public enum RoleType {
    ROLE_USER,      // A basic, default role
    ROLE_ADMIN,     // Full system administrator
    ROLE_WAREHOUSE, // Can manage inventory
    ROLE_ACCOUNTING // Can manage invoices
}
