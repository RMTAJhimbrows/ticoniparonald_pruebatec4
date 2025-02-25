package com.hackaboss.app.services;

import com.hackaboss.app.dtos.VueloDTO;
import com.hackaboss.app.entities.Vuelo;
import com.hackaboss.app.exceptions.BusinessException;
import com.hackaboss.app.exceptions.ResourceNotFoundException;
import com.hackaboss.app.repositories.IVueloRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VueloService implements IVueloService{
    private final IVueloRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<VueloDTO> findAll() {
        List<Vuelo> vuelos = repository.findAllByDeletedFalse();
        if (vuelos.isEmpty()){
            throw new BusinessException("No hay vuelos disponibles ");
        }

        return vuelos.stream()
                .map(vuelo -> modelMapper.map(vuelo, VueloDTO.class))
                .toList();
    }
}
