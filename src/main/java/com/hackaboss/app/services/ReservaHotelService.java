package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.dtos.ReservaHotelDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.entities.Huesped;
import com.hackaboss.app.entities.ReservaHotel;
import com.hackaboss.app.exceptions.BusinessException;
import com.hackaboss.app.repositories.IHotelRepository;
import com.hackaboss.app.repositories.IReservaHotelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaHotelService implements IReservaHotelService {
    private final IReservaHotelRepository reservaRepository;
    private final IHotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private  final DateTimeFormatter DATE_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Crea una reserva de hotel y devuelve el monto total.
     *
     * @param reservaHotelDTO Datos de la reserva.
     * @return TotalMountDTO con el precio total calculado.
     */
    @Override
    public TotalMountDTO createdReservation(ReservaHotelDTO reservaHotelDTO) {

        if (reservaHotelDTO.getFechaEntrada() == null || reservaHotelDTO.getFechaSalida() == null) {
            throw new BusinessException("Las fechas de entrada y salida son obligatorias.");
        }

        if (reservaHotelDTO.getCantidadPersonas() == null || reservaHotelDTO.getCantidadPersonas() <= 0){
            throw new BusinessException("La cantidad de personas debe ser mayor que cero. ");
        }

        if (reservaHotelDTO.getHuespedesDTO() == null || reservaHotelDTO.getHuespedesDTO().size() != reservaHotelDTO.getCantidadPersonas()){
            throw new BusinessException("La cantidad de huéspedes proporcionados no coincide con 'cantidadPersonas'");
        }

        if (reservaHotelDTO.getCodigoHotel() == null || reservaHotelDTO.getCodigoHotel().isEmpty()){
            throw new BusinessException("El código de hotel no puede ser null o no puede estar vacío.");
        }

        Hotel hotel = hotelRepository.findByCodigoHotel(reservaHotelDTO.getCodigoHotel())
                .orElseThrow(() -> new BusinessException("hotel no encontrado: " + reservaHotelDTO.getCodigoHotel()));

        if (hotel.getReservado()){
            throw new BusinessException("El hotel y las habitaciones están reservadas. Por favor elija otra. ");
        }

        double precioTotal = calcularMontoTotal(reservaHotelDTO, hotel);

        ReservaHotel reservaHotel = convertToEntity(reservaHotelDTO, hotel);
        reservaHotel.setMontoTotal(precioTotal);
        hotel.setReservado(true);

        reservaRepository.save(reservaHotel);
        return new TotalMountDTO(precioTotal);
    }

    /**
     * Obtiene una lista de habitaciones disponibles basadas en el destino y las fechas de entrada y salida.
     *
     * @param destination Destino para filtrar las habitaciones disponibles.
     * @param dateFrom Fecha de entrada para filtrar las habitaciones disponibles.
     * @param dateTo Fecha de salida para filtrar las habitaciones disponibles.
     * @return List<HotelDTO> Lista de habitaciones disponibles con sus respectivos datos.
     * @throws BusinessException Si no se encuentran habitaciones disponibles con los criterios proporcionados.
     */
    @Override
    public List<HotelDTO> obtenerHabitacionesDisponibles(String destination, LocalDate dateFrom, LocalDate dateTo) {
        List<Hotel> listado = hotelRepository.findAll();

        if (listado.isEmpty()){
            throw new BusinessException("No hay habitaciones registrados.");
        }

        List<HotelDTO> hotelesDisponibles = listado.stream()
                .filter(hotel -> !hotel.isDeleted())
                .filter(hotel -> !hotel.getReservado())
                .filter(hotel -> destination == null || hotel.getLugar().equalsIgnoreCase(destination))
                .filter(hotel -> dateFrom == null || !hotel.getDisponibleDesde().isBefore(dateFrom))
                .filter(hotel -> dateTo == null || !hotel.getDisponibleHasta().isAfter(dateTo))
                .map(hotel -> modelMapper.map(hotel, HotelDTO.class)).toList();

        if (hotelesDisponibles.isEmpty()){
            throw new BusinessException("No se encontraron habitaciones disponibles para los criterios especificados.");
        }
        return hotelesDisponibles;
    }

    /**
     * Calcula el monto total de la reserva basado en las fechas de entrada y salida, el precio por noche del hotel y la cantidad de personas.
     *
     * @param reservaHotelDTO Datos de la reserva del hotel, incluyendo fechas, cantidad de personas y código del hotel.
     * @param hotel El hotel al cual se hace la reserva, con el precio por noche.
     * @return double El monto total de la reserva calculado.
     * @throws BusinessException Si las fechas no son válidas o si la reserva es menor a un día.
     */
    @Override
    public double calcularMontoTotal(ReservaHotelDTO reservaHotelDTO, Hotel hotel) {
        LocalDate fechaEntrada = parseFecha(reservaHotelDTO.getFechaEntrada());
        LocalDate fechaSalida = parseFecha(reservaHotelDTO.getFechaSalida());

        if (fechaSalida.isBefore(fechaEntrada)){
            throw new BusinessException("La fecha de salida no puede ser anterior a la fecha de entrada. ");
        }
        long diasTotales = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        if (diasTotales <= 0){
            throw new BusinessException("La reserva debe ser al menos de un día.");
        }

        return diasTotales * hotel.getPrecioNoche() * reservaHotelDTO.getCantidadPersonas();
    }

    /**
     * Convierte un DTO de reserva de hotel en una entidad ReservaHotel.
     *
     * @param dto El objeto DTO con los datos de la reserva del hotel.
     * @param hotel El hotel asociado a la reserva.
     * @return ReservaHotel La entidad que representa la reserva del hotel.
     */
    @Override
    public ReservaHotel convertToEntity(ReservaHotelDTO dto, Hotel hotel) {
        ReservaHotel reserva = new ReservaHotel();
        reserva.setFechaEntrada(parseFecha(dto.getFechaEntrada()));
        reserva.setFechaSalida(parseFecha(dto.getFechaSalida()));
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setHotel(hotel);
        reserva.setHuespedes(mapearHuespedes(dto, reserva));
        return reserva;
    }

    /**
     * Parsea una fecha en formato dd/MM/yyyy a LocalDate.
     *
     * @param fechaStr La fecha como cadena de texto que debe ser convertida.
     * @return LocalDate La fecha convertida a tipo LocalDate.
     * @throws BusinessException Si el formato de la fecha es incorrecto.
     */
    private LocalDate parseFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, DATE_FORMATO);
        } catch (Exception e) {
          throw new BusinessException("Formato de fecha inválido. Utilice el siguiente formato dd/MM/yyyy.");
        }
    }

    /**
     * Mapea una lista de huéspedes desde el DTO a entidades Huesped asociadas a la reserva.
     *
     * @param dto El DTO con los datos de los huéspedes.
     * @param reserva La reserva de hotel asociada a los huéspedes.
     * @return List<Huesped> Una lista de entidades Huesped asociadas a la reserva.
     * @throws BusinessException Si alguno de los huéspedes no tiene nombre o apellido.
     */
    private List<Huesped> mapearHuespedes(ReservaHotelDTO dto, ReservaHotel reserva) {
        if (dto.getHuespedesDTO() == null) return List.of();
        return dto.getHuespedesDTO().stream()
                .map(huespedDTO -> {
                    if (huespedDTO.getNombre() == null || huespedDTO.getApellido() == null) {
                        throw new BusinessException("El nombre y apellido del huesped son obligatorios.");
                    }
                    Huesped huesped = new Huesped();
                    huesped.setNombre(huespedDTO.getNombre());
                    huesped.setApellido(huespedDTO.getApellido());
                    huesped.setReservaHotel(reserva);
                    return huesped;
                })
                .toList();
    }
}
