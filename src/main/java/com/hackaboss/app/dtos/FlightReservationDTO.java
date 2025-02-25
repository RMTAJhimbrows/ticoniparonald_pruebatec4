package com.hackaboss.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightReservationDTO {
    private Long id;
    private String flightDate;    // corresponde a fechaVuelo
    private Integer numberOfPeople; // corresponde a cantidadPersonas
    private Double totalAmount;   // corresponde a montoTotal
    private VueloDTO flight;
    private List<PassengerDTO> passengers;
}
