package com.hackaboss.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private Long identifier;
    private String hotelCode;
    private String name;
    private String location;
    private String roomType;
    private Double nightPrice;
    private String availableFrom;
    private String availableTo;
    private Boolean booked;

}
