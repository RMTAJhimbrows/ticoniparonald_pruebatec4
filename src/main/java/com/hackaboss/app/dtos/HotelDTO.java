package com.hackaboss.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {

    @JsonProperty("identifier")
    private Long id;
    @JsonProperty("hotelCode")
    private String codigoHotel;
    @JsonProperty("name")
    private String nombre;
    @JsonProperty("location")
    private String lugar;
    @JsonProperty("roomType")
    private String tipoHabitacion;
    @JsonProperty("nightPrice")
    private Double precioNoche;
    @JsonProperty("availableFrom")
    private LocalDate disponibleDesde;
    @JsonProperty("availableTo")
    private LocalDate disponibleHasta;
    @JsonProperty("booked")
    private Boolean reservado;

}
