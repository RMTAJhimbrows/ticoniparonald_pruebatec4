package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;

import java.time.LocalDate;
import java.util.List;

public interface IHotelService {
    List<HotelDTO> obtenerHoteles();
    List<HotelDTO> getAvailableRooms(String destination, LocalDate dateFrom, LocalDate dateTo);

    HotelDTO guardarHotel(HotelDTO dto);
    HotelDTO actualizarHotel(Long id, HotelDTO dto);
    HotelDTO buscarHotelPorID(Long id);
    List<HotelDTO> eliminarHotel(Long id);
}
