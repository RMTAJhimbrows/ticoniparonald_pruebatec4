package com.hackaboss.app.services;

import com.hackaboss.app.dtos.VueloDTO;
import com.hackaboss.app.exceptions.BusinessException;
import com.hackaboss.app.repositories.IVueloRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VueloService implements IVueloService {
    private final IVueloRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public Map<String, List<VueloDTO>> obtenerVuelos(LocalDate fechaIda, LocalDate fechaVuelta, String origen, String destino) {
        if (fechaIda == null && fechaVuelta == null && origen == null && destino == null) {
            List<VueloDTO> vuelos = repository.findAllByDeletedFalse().stream().
                    map(vuelo -> modelMapper.map(vuelo, VueloDTO.class))
                    .toList();
            if (vuelos.isEmpty()) {
                throw new BusinessException("No hay vuelos disponibles ");
            }
            return Map.of("vuelos", vuelos);
        }

        List<VueloDTO> vuelosIda = filtrarVuelos(fechaIda, null, origen, destino);
        List<VueloDTO> vuelosVuelta = filtrarVuelos(null, fechaVuelta, destino, origen);

        return Map.of("vuelosIda", vuelosIda, "vuelosVuelta", vuelosVuelta);

    }

    private List<VueloDTO> filtrarVuelos(LocalDate fechaIda, LocalDate fechaVuelta, String origen, String destino) {
        return repository.findAll().stream()
                .filter(vuelo -> !vuelo.isDeleted())
                .filter(vuelo -> fechaIda == null || !vuelo.getFechaIda().isBefore(fechaIda))
                .filter(vuelo -> fechaVuelta == null || !vuelo.getFechaVuelta().isAfter(fechaVuelta))
                .filter(vuelo -> origen == null || vuelo.getOrigen().equalsIgnoreCase(origen))
                .filter(vuelo -> destino == null || vuelo.getDestino().equalsIgnoreCase(destino))
                .map(vuelo -> modelMapper.map(vuelo, VueloDTO.class))
                .toList();
    }
}
