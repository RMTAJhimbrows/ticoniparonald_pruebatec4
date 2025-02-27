package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.exceptions.BusinessException;
import com.hackaboss.app.exceptions.ResourceNotFoundException;
import com.hackaboss.app.repositories.IHotelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    @Override
    public HotelDTO guardarHotel(HotelDTO dto) {
        Optional<Hotel> hotelExistente = repository.findByCodigoHotel(dto.getCodigoHotel());
        if (hotelExistente.isPresent()) {
            throw new BusinessException("El código de hotel ya existe.");
        }

        LocalDate fechaDesde = dto.getDisponibleDesde();
        LocalDate fechaHasta = dto.getDisponibleHasta();
        validarFecha(fechaDesde, fechaHasta);

        Hotel nuevoHotel = crearHotelDesdeDTO(dto);

        Hotel hotelGuardado = repository.save(nuevoHotel);

        return modelMapper.map(hotelGuardado, HotelDTO.class);
    }

    @Override
    public HotelDTO actualizarHotel(Long id, HotelDTO dto) {

        Hotel hotelEncontrado = repository.findById(id).orElseThrow(() -> new BusinessException("Hotel no encontrado con id: " + id));
        if (hotelEncontrado.isDeleted()) {
            throw new BusinessException("Hotel con ID ha sido eliminado. " + id);
        }

        LocalDate fechaDesde = dto.getDisponibleDesde();
        LocalDate fechaHasta = dto.getDisponibleHasta();

        validarFecha(fechaDesde, fechaHasta);

        actualizarHotelDesdeDTO(hotelEncontrado, dto);
        Hotel hotelGuardado = repository.save(hotelEncontrado);

        return modelMapper.map(hotelGuardado, HotelDTO.class);
    }

    @Override
    public HotelDTO buscarHotelPorID(Long id) {
        if (id <= 0) {
            throw new BusinessException("El id es obligatorio y debe ser numero positivo ");
        }
        Hotel hotelBuscado = repository.findById(id).orElseThrow(() -> new BusinessException("El id proporcionado no encontrado: " + id));
        if (hotelBuscado.isDeleted()) {
            throw new BusinessException("El hotel con ID ha sido eliminado");
        }
        return modelMapper.map(hotelBuscado, HotelDTO.class);
    }

    @Override
    public List<HotelDTO> eliminarHotel(Long id) {
        if (id <= 0) {
            throw new BusinessException("El id es obligatorio y debe ser numero positivo ");
        }

        Hotel hotelBuscado = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Hotel no encontrado con id: " + id));
        if (hotelBuscado.isDeleted()) {
            throw new BusinessException("El hotel ya ha sido eliminado");
        }
        hotelBuscado.setDeleted(true);
        repository.save(hotelBuscado);

        List<Hotel> listado = repository.findAllByDeletedFalse();
        return listado.stream().map(hotel -> modelMapper.map(hotel, HotelDTO.class))
                .toList();
    }

    // Métodos auxiliares
    private void actualizarHotelDesdeDTO(Hotel hotel, HotelDTO dto) {
        hotel.setCodigoHotel(dto.getCodigoHotel());
        hotel.setNombre(dto.getNombre());
        hotel.setLugar(dto.getLugar());
        hotel.setTipoHabitacion(dto.getTipoHabitacion());
        hotel.setPrecioNoche(dto.getPrecioNoche());
        hotel.setDisponibleDesde(dto.getDisponibleDesde());
        hotel.setDisponibleHasta(dto.getDisponibleHasta());
        hotel.setReservado(dto.getReservado());
    }
    private void validarFecha(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha Ida no puede ser anterior a la fecha actual.");
        }
        if (fechaHasta.isBefore(fechaDesde)) {
            throw new BusinessException("La fecha de Desde no puede ser anterior a la fecha de ida.");
        }
        if (fechaHasta.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de Hasta no puede ser anterior a la fecha actual. ");
        }
    }
    private Hotel crearHotelDesdeDTO(HotelDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setCodigoHotel(dto.getCodigoHotel());
        hotel.setNombre(dto.getNombre());
        hotel.setLugar(dto.getLugar());
        hotel.setTipoHabitacion(dto.getTipoHabitacion());
        hotel.setPrecioNoche(dto.getPrecioNoche());
        hotel.setDisponibleDesde(dto.getDisponibleDesde());
        hotel.setDisponibleHasta(dto.getDisponibleHasta());
        hotel.setReservado(dto.getReservado());
        return hotel;
    }
}