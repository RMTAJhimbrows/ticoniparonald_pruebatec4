package com.hackaboss.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
