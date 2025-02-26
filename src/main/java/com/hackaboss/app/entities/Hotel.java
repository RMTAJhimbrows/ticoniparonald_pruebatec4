package com.hackaboss.app.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "hoteles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigoHotel;
    private String nombre;
    private String lugar;
    private String tipoHabitacion;
    private Double precioNoche;
    private LocalDate disponibleDesde;
    private LocalDate disponibleHasta;
    private Boolean reservado;

    @JsonManagedReference
    @OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReservaHotel> reservas;

    private boolean deleted;

}
