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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {

    private final IHotelRepository repository;
    private final ModelMapper modelMapper;


    /**
     * Obtiene una lista de todos los hoteles que no han sido eliminados.
     *
     * @return List<HotelDTO> Lista de hoteles disponibles, convertidos a DTO.
     * @throws ResourceNotFoundException Si no se encuentran hoteles registrados.
     */
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

    /**
     * Guarda un nuevo hotel en el sistema. Si el código de hotel ya existe, se lanzará una excepción.
     *
     * @param dto Datos del nuevo hotel que se desea guardar.
     * @return HotelDTO El hotel guardado, convertido a DTO.
     * @throws BusinessException Si el código de hotel ya existe o si las fechas no son válidas.
     */
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

    /**
     * Actualiza la información de un hotel existente. Si el hotel está eliminado, se lanzará una excepción.
     *
     * @param id Identificador del hotel a actualizar.
     * @param dto Nuevos datos del hotel.
     * @return HotelDTO El hotel actualizado, convertido a DTO.
     * @throws BusinessException Si el hotel no se encuentra o si las fechas no son válidas.
     */
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

    /**
     * Busca un hotel por su ID. Si el hotel no existe o está eliminado, se lanzará una excepción.
     *
     * @param id Identificador del hotel a buscar.
     * @return HotelDTO El hotel encontrado, convertido a DTO.
     * @throws BusinessException Si el ID es inválido o si el hotel no se encuentra o está eliminado.
     */
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

    /**
     * Elimina un hotel (cambiando su estado a eliminado). Devuelve la lista de hoteles no eliminados.
     *
     * @param id Identificador del hotel a eliminar.
     * @return List<HotelDTO> Lista de hoteles no eliminados, convertidos a DTO.
     * @throws BusinessException Si el ID es inválido o si el hotel no se encuentra o ya está eliminado.
     */
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
    /**
     * Actualiza los atributos de un hotel con los datos proporcionados en el DTO.
     *
     * @param hotel El hotel que se va a actualizar.
     * @param dto El DTO con los nuevos datos del hotel.
     */
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

    /**
     * Valida las fechas de disponibilidad del hotel.
     *
     * @param fechaDesde Fecha de inicio de la disponibilidad.
     * @param fechaHasta Fecha de fin de la disponibilidad.
     * @throws BusinessException Si alguna de las fechas no es válida.
     */
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

    /**
     * Crea un objeto Hotel a partir de un DTO.
     *
     * @param dto El DTO con los datos del hotel.
     * @return Hotel El objeto Hotel creado.
     */
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