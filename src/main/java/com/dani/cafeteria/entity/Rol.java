package com.dani.cafeteria.entity;

/**
 * Enum que define los roles del sistema.
 * PROFESOR: puede reservar menus y rellenar encuestas.
 * COCINERO: puede crear menus, platos y gestionar el sistema.
 * ADMIN: puede gestionar usuarios y tiene acceso total al sistema.
 */
public enum Rol {
    PROFESOR,
    COCINERO,
    ADMIN
}
