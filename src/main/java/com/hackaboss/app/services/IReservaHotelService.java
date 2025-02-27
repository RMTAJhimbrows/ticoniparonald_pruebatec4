package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.dtos.ReservaHotelDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.entities.ReservaHotel;

import java.time.LocalDate;
import java.util.List;

public interface IReservaHotelService {
    TotalMountDTO createdReservation(ReservaHotelDTO reservaHotelDTO);
    List<HotelDTO> obtenerHabitacionesDisponibles(String destination, LocalDate dateFrom, LocalDate dateTo);

    double calcularMontoTotal(ReservaHotelDTO reservaHotelDTO, Hotel hotel);
    ReservaHotel convertToEntity(ReservaHotelDTO dto, Hotel hotel);

}
