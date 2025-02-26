package com.hackaboss.app.services;

import com.hackaboss.app.dtos.ReservaHotelDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.entities.Huesped;
import com.hackaboss.app.entities.ReservaHotel;
import com.hackaboss.app.exceptions.BusinessException;
import com.hackaboss.app.repositories.IHotelRepository;
import com.hackaboss.app.repositories.IReservaHotelRepository;
import lombok.RequiredArgsConstructor;
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
    private  final DateTimeFormatter DATE_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    private LocalDate parseFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, DATE_FORMATO);
        } catch (Exception e) {
          throw new BusinessException("Formato de fecha inválido. Use yyyy-MM-dd.");
        }
    }

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
