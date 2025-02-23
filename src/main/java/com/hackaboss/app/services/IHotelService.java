package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;

import java.util.List;

public interface IHotelService {
    List<HotelDTO> findAll();
}
