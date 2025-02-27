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

    /**
     * Obtiene el precio total de una reserva de vuelo y guarda la reserva en la base de datos.
     *
     * @param reservaVueloDTO Datos de la reserva incluyendo código de vuelo, pasajeros y fechas.
     * @return TotalMountDTO con el monto total calculado.
     */
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

    /**
     * Calcula el precio total de la reserva de vuelo.
     *
     * @param dto   Datos de la reserva.
     * @param vuelo Datos del vuelo reservado.
     * @return El monto total a pagar.
     */
    @Override
    public Double calcularPrecioTotal(ReservaVueloDTO dto, Vuelo vuelo) {
        return dto.getCantidadPersonas() * vuelo.getPrecioPorPersona();
    }


    /**
     * Convierte un DTO de reserva de vuelo en una entidad ReservaVuelo.
     *
     * @param dto   DTO con los datos de la reserva.
     * @param vuelo Entidad de vuelo asociada a la reserva.
     * @return Objeto ReservaVuelo listo para ser persistido.
     */
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

    /**
     * Mapea los pasajeros del DTO a entidades Pasajero asociadas a la reserva.
     *
     * @param dto     DTO de la reserva con la lista de pasajeros.
     * @param reserva ReservaVuelo a la que pertenecen los pasajeros.
     * @return Lista de entidades Pasajero.
     */
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
