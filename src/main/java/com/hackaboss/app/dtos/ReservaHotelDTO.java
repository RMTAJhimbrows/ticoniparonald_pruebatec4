package com.hackaboss.app.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ReservaHotelDTO {

    @JsonProperty("identifier")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("checkIn")
    private String fechaEntrada;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("checkOut")
    private String fechaSalida;

    @JsonProperty("numberOfPeople")
    private Integer cantidadPersonas;

    @JsonProperty("hotel")
    private String codigoHotel;

    @JsonProperty("guests")
    private List<HuespedDTO> huespedesDTO;
}
