package com.hackaboss.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDTO {
    private Long id;
    private String firstName; // corresponde a nombre
    private String lastName;  // corresponde a apellido
}
