package com.hackaboss.app.services;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.entities.Hotel;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("1")
    void testFindAllConHotelesExistentes() {
        // Arrange
        List<Hotel> hoteles = List.of(
                new Hotel(1L, "XX-00034", "Hotel Radisson", "La Paz", "Doble", 650.00,
                        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), false, false),
                new Hotel(2L, "XX-00035", "Hotel España", "Madrid", "Triple", 850.00,
                        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), false, false)
        );

        when(repository.findAllByDeletedFalse()).thenReturn(hoteles);

        // Act
        List<HotelDTO> resultado = service.findAll();

        // Assert
        assertThat(resultado).hasSize(2); // ✅ Lista con 2 hoteles
        verify(repository).findAllByDeletedFalse(); // ✅ Verifica interacción con el repositorio

        // Verifica mapeo correcto del primer hotel
        assertThat(resultado.get(0))
                .extracting(
                        HotelDTO::getId,
                        HotelDTO::getCodigoHotel,
                        HotelDTO::getLugar
                )
                .containsExactly(
                        1L,
                        "XX-00034",
                        "La Paz"
                );
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando no hay hoteles registrados")
    void testFindAllSinHoteles() {
        when(repository.findAllByDeletedFalse()).thenReturn(new ArrayList<>());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findAll()
        );

        assertThat(exception.getMessage()).isEqualTo("No hay hoteles registrados.");
        verify(repository).findAllByDeletedFalse();
    }
}
