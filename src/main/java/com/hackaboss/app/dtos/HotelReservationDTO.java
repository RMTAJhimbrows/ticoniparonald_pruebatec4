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
public class HotelReservationDTO {
    private Long id;
    private String checkIn;       // corresponde a fechaEntrada
    private String checkOut;      // corresponde a fechaSalida
    private Integer numberOfPeople; // corresponde a cantidadPersonas
    private Double totalAmount;   // corresponde a montoTotal
    private HotelDTO hotel;
    private List<GuestDTO> guests;
}
