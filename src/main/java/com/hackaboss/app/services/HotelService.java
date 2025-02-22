package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.repositories.IHotelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService{

    private final IHotelRepository repository;
    private final ModelMapper modelMapper;


    @Override
    public List<HotelDTO> getAllHotels() {
        return repository.findAll().stream()
                .map(hotel -> modelMapper.map(hotel, HotelDTO.class))
                .toList();
    }
}
