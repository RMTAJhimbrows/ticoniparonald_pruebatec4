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
public class HuespedDTO {
    private Long id;
    @JsonProperty("firstName")
    private String nombre;
    @JsonProperty("lastName")
    private String apellido;
}
