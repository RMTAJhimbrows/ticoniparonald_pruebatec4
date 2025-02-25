package com.hackaboss.app.services;

import com.hackaboss.app.dtos.ReservaHotelDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.entities.ReservaHotel;

public interface IHotelReservationService {
    TotalMountDTO createdReservation(ReservaHotelDTO reservaHotelDTO);
    double calcularMontoTotal(ReservaHotelDTO reservaHotelDTO, Hotel hotel);
    ReservaHotel convertToEntity(ReservaHotelDTO dto, Hotel hotel);

}
