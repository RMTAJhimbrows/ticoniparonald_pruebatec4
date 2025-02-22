package com.hackaboss.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "huespedes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Huesped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @ManyToOne
    @JoinColumn(name = "reserva_hotel_id", nullable = false)
    private ReservaHotel reservaHotel;
}
