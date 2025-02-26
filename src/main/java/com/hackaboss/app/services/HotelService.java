package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.exceptions.ResourceNotFoundException;
import com.hackaboss.app.repositories.IHotelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {

    private final IHotelRepository repository;
    private final ModelMapper modelMapper;


    @Override
    public List<HotelDTO> obtenerHoteles() {
        List<Hotel> hoteles = repository.findAllByDeletedFalse();
        if (hoteles.isEmpty()) {
            throw new ResourceNotFoundException("No hay hoteles registrados.");
        }
        return hoteles.stream()
                .map(hotel -> modelMapper.map(hotel, HotelDTO.class))
                .toList();
    }

    @Override
    public List<HotelDTO> getAvailableRooms(String destination, LocalDate dateFrom, LocalDate dateTo) {

        List<Hotel> listado = repository.findAll();

        return repository.findAll().stream()
                .filter(hotel -> !hotel.isDeleted())
                .filter(hotel -> !hotel.getReservado())
                .filter(hotel -> destination == null || hotel.getLugar().equalsIgnoreCase(destination))
                .filter(hotel -> dateFrom == null || !hotel.getDisponibleDesde().isBefore(dateFrom))
                .filter(hotel -> dateTo == null || !hotel.getDisponibleHasta().isAfter(dateTo))
                .map(hotel -> modelMapper.map(hotel, HotelDTO.class)).toList();
    }
}