package com.dani.cafeteria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidad Reserva.
 * Relacion N:M entre Usuario y Menu (entidad asociativa).
 * Un profesor puede reservar muchos menus y un menu puede ser reservado por muchos profesores.
 */
@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_menu", nullable = false)
    private Menu menu;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(length = 50)
    private String estado;

    @Column(name = "codigo_recogida", length = 50)
    private String codigoRecogida;

    @Column(nullable = false)
    private Boolean asistido;
}
