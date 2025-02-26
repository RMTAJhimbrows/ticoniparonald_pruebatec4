package com.hackaboss.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VueloDTO {

    @JsonProperty("")
    private Long id;

    @JsonProperty("flightNumber")
    private String codigoVuelo;

    @JsonProperty("origin")
    private String origen;

    @JsonProperty("destination")
    private String destino;

    @JsonProperty("seatType")
    private String tipoAsiento;

    @JsonProperty("pricePerPerson")
    private Double precioPorPersona;

    @JsonProperty("departureDate")
    private String fechaIda;

    @JsonProperty("returnDate")
    private String fechaVuelta;
}
