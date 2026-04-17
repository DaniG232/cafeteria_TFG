package com.dani.cafeteria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad Plato.
 * Tipos: PRIMERO, SEGUNDO, POSTRE, BEBIDA.
 */
@Entity
@Table(name = "plato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plato")
    private Integer idPlato;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPlato tipo;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Boolean activo;

    public enum TipoPlato {
        PRIMERO, SEGUNDO, POSTRE, BEBIDA
    }
}
