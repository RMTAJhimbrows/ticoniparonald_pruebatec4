package com.hackaboss.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pasajeros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @ManyToOne
    @JoinColumn(name = "reserva_vuelo_id", nullable = false)
    private ReservaVuelo reservaVuelo;
}
