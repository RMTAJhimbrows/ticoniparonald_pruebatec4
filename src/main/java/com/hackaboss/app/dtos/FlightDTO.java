package com.hackaboss.app.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {

    private Long id;
    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private Double pricePerPerson;
    private String departureDate;
    private String returnDate;
}
