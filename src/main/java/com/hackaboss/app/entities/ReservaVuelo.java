package com.hackaboss.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reservas_vuelos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaVuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaVuelo;
    private Integer cantidadPersonas;
    private Double montoTotal;

    @ManyToOne
    @JoinColumn(name = "vuelo_id", nullable = false)
    private Vuelo vuelo;

    @OneToMany(mappedBy = "reservaVuelo", cascade = CascadeType.ALL)
    private List<Pasajero> pasajeros;
}
