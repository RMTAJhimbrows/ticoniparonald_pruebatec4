package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;

import java.time.LocalDate;
import java.util.List;

public interface IHotelService {
    List<HotelDTO> findAll();

    List<HotelDTO> getAvailableRooms(String destination, LocalDate dateFrom, LocalDate dateTo);

}
