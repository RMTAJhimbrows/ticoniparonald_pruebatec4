package com.hackaboss.app.services;

import com.hackaboss.app.dtos.ReservaVueloDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.entities.Pasajero;
import com.hackaboss.app.entities.ReservaVuelo;
import com.hackaboss.app.entities.Vuelo;
import com.hackaboss.app.exceptions.BusinessException;
import com.hackaboss.app.repositories.IReservaVueloRepository;
import com.hackaboss.app.repositories.IVueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaVueloService implements IReservaVueloService{

    private final IReservaVueloRepository repository;
    private final IVueloRepository vueloRepository;
    private  final DateTimeFormatter DATE_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public TotalMountDTO obtenerPrecioTotal(ReservaVueloDTO reservaVueloDTO) {

        if (reservaVueloDTO.getFechaVuelo() == null || reservaVueloDTO.getDestino() == null || reservaVueloDTO.getCantidadPersonas() == null || reservaVueloDTO.getOrigen() == null || reservaVueloDTO.getCodigoVuelo() == null || reservaVueloDTO.getPasajeros() == null
         || reservaVueloDTO.getFechaVuelo().isEmpty() || reservaVueloDTO.getOrigen().isEmpty() || reservaVueloDTO.getDestino().isEmpty() || reservaVueloDTO.getPasajeros().isEmpty()){
            throw new BusinessException("Uno de los datos o mas están vacíos o son nulos.");
        }
        if (reservaVueloDTO.getCantidadPersonas() <= 0){
            throw new BusinessException("La cantidad de pasajeros debe ser mayor que cero. ");
        }
        if (reservaVueloDTO.getPasajeros().size() != reservaVueloDTO.getCantidadPersonas()){
            throw new BusinessException("La cantidad de pasajeros no coincide con los pasajeros proporcionados");
        }
        if (reservaVueloDTO.getCodigoVuelo().isEmpty()){
            throw new BusinessException("Es obligatorio el código de vuelo");
        }

        Vuelo vuelo = vueloRepository.findByCodigoVuelo(reservaVueloDTO.getCodigoVuelo())
                .orElseThrow(() -> new BusinessException("Vuelo no encontrado"));

        if (vuelo.isDeleted()){
            throw new BusinessException("El vuelo no se encuentra disponible. Elija otra vuelo.");
        }

        double precioTotal = calcularPrecioTotal(reservaVueloDTO, vuelo);
        ReservaVuelo reserva = convertToEntity(reservaVueloDTO, vuelo);
        reserva.setMontoTotal(precioTotal);
        vuelo.setDeleted(true);

        repository.save(reserva);

        return new TotalMountDTO(precioTotal);
    }

    @Override
    public Double calcularPrecioTotal(ReservaVueloDTO dto, Vuelo vuelo) {
        return dto.getCantidadPersonas() * vuelo.getPrecioPorPersona();
    }

    @Override
    public ReservaVuelo convertToEntity(ReservaVueloDTO dto, Vuelo vuelo) {
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(dto.getFechaVuelo(), DATE_FORMATO);

        } catch (Exception e) {
            throw new BusinessException("Formato de fecha no válido. Por favor verifique el siguiente formato dd/MM/yyyy");
        }

        ReservaVuelo reserva = new ReservaVuelo();

        reserva.setFechaVuelo(fecha);
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setOrigen(dto.getOrigen());
        reserva.setDestino(dto.getDestino());
        reserva.setVuelo(vuelo);
        reserva.setPasajeros(mapearPasajeros(dto, reserva));
        return reserva;
    }

    private List<Pasajero> mapearPasajeros(ReservaVueloDTO dto, ReservaVuelo reserva) {
        if (dto.getPasajeros() == null) return List.of();
        return dto.getPasajeros().stream()
                .map(pasajeroDTO -> {
                   if (pasajeroDTO.getNombre() == null || pasajeroDTO.getApellido() == null){
                       throw new BusinessException("El Nombre y el Apellido son obligatorios. ");
                   }
                   Pasajero pasajero = new Pasajero();
                   pasajero.setNombre(pasajeroDTO.getNombre());
                   pasajero.setApellido(pasajeroDTO.getApellido());
                   pasajero.setReservaVuelo(reserva);
                   return pasajero;
                }).toList();
    }
}
