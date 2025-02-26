package com.hackaboss.app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaVueloDTO {

    private Long id;

    @JsonProperty("date")
    private String fechaVuelo;

    @JsonProperty("peopleQ")
    private Integer cantidadPersonas;

    @JsonProperty("origin")
    private String origen;

    @JsonProperty("destination")
    private String destino;

    @JsonProperty("flightCode")
    private String codigoVuelo;

    @JsonProperty("passengers")
    private List<PasajeroDTO> pasajeros;
}
