package com.hackaboss.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reservas_hoteles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaHotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private Integer cantidadPersonas;
    private Double montoTotal;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "reservaHotel", cascade = CascadeType.ALL)
    private List<Huesped> huespedes;
}
