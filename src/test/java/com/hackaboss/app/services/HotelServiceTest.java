package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.entities.Huesped;
import com.hackaboss.app.entities.ReservaHotel;
import com.hackaboss.app.exceptions.ResourceNotFoundException;
import com.hackaboss.app.repositories.IHotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    // TODO: Inyectar el mock en el servicio
    @InjectMocks
    private HotelService service;

    // TODO: Simulación del mock de mi repositorio
    @Mock
    private IHotelRepository repository;
    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp(){
        service = new HotelService(repository, modelMapper);
    }


    @Test
    @DisplayName("Se envía solicitud de listado de todos los hoteles registrados - 1")
    void testObtenerHotelesConHotelesExistentes() {

        Hotel hotelRadison = new Hotel(
        1L, "XX-00034", "Hotel Radisson", "La Paz", "Doble", 650.00,
                LocalDate.of(2024, 12, 22), LocalDate.of(2024, 12, 31),
                false, new ArrayList<>(), false
        );

        ReservaHotel reserva = new ReservaHotel(
                1L,
                LocalDate.of(2024, 1, 22),
                LocalDate.of(2024, 12, 31),
                2,
                15300.00,
                hotelRadison,
                new ArrayList<>()
        );

        List<Huesped> huespeds = List.of(
                new Huesped(1L, "Juan", "García", reserva),
                new Huesped(2L, "Berta", "García", reserva)
        );

        reserva.setHuespedes(huespeds);
        hotelRadison.setReservas(List.of(reserva));

        Hotel hotelEspania = new Hotel(
                2L, "XX-00035", "Hotel España", "Madrid", "Triple", 850.00,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31),
                false, new ArrayList<>(), false
        );

        Hotel hotelVIP = new Hotel(
                2L, "XX-00035", "Hotel VIP", "Valencia", "Triple", 850.00,
                LocalDate.of(2024, 3, 10), LocalDate.of(2024, 4, 1),
                true, new ArrayList<>(), true
        );

        List<Hotel> hoteles = List.of(hotelRadison, hotelEspania);

        when(repository.findAllByDeletedFalse()).thenReturn(hoteles);

        List<HotelDTO> resultado = service.obtenerHoteles();

        assertThat(resultado).hasSize(2);

        verify(repository).findAllByDeletedFalse();

    }

    @Test
    @DisplayName("Se envía solicitud de listado de todos los hoteles registrados - 2")
    void testObtenerHotelesSinHoteles() {
        when(repository.findAllByDeletedFalse()).thenReturn(List.of());
        assertThatThrownBy(() -> service.obtenerHoteles())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No hay hoteles registrados.");
    }
}
