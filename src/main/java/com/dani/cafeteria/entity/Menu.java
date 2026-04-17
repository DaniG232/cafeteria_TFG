package com.dani.cafeteria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidad Menu.
 * Tiene 4 FKs a Plato: primer plato, segundo plato, postre y bebida.
 * Segun el modelo logico de la Fase 1.
 */
@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_menu")
    private Integer idMenu;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_primer_plato", nullable = false)
    private Plato primerPlato;

    @ManyToOne
    @JoinColumn(name = "id_segundo_plato", nullable = false)
    private Plato segundoPlato;

    @ManyToOne
    @JoinColumn(name = "id_postre", nullable = false)
    private Plato postre;

    @ManyToOne
    @JoinColumn(name = "id_bebida", nullable = false)
    private Plato bebida;
}
