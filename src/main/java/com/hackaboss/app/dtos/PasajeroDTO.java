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
public class PasajeroDTO {

    private Long id;

    @JsonProperty("name")
    private String nombre;

    @JsonProperty("lastname")
    private String apellido;
}
