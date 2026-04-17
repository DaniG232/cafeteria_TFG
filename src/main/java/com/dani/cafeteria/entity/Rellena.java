package com.dani.cafeteria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entidad Rellena.
 * Relacion N:M entre Usuario y Encuesta.
 * PK compuesta: (email, id_encuesta).
 */
@Entity
@Table(name = "rellena")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Rellena.RellenaId.class)
public class Rellena {

    @Id
    @ManyToOne
    @JoinColumn(name = "email")
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_encuesta")
    private Encuesta encuesta;

    @Column(name = "fecha_respuesta")
    private LocalDate fechaRespuesta;

    /**
     * Clase para la clave primaria compuesta.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class RellenaId implements Serializable {
        private String usuario;   // corresponde al campo email (PK de Usuario)
        private Integer encuesta; // corresponde al campo idEncuesta (PK de Encuesta)
    }
}
