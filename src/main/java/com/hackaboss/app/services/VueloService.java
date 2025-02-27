package com.hackaboss.app.services;

import com.hackaboss.app.dtos.VueloDTO;
import com.hackaboss.app.entities.Vuelo;
import com.hackaboss.app.exceptions.BusinessException;
import com.hackaboss.app.repositories.IVueloRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VueloService implements IVueloService {

    private final IVueloRepository repository;
    private final ModelMapper modelMapper;
    private final DateTimeFormatter DATE_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Map<String, List<VueloDTO>> obtenerVuelosFiltrados(LocalDate fechaIda, LocalDate fechaVuelta, String origen, String destino) {
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

    @Override
    public VueloDTO guardarVuelo(VueloDTO dto) {
        Optional<Vuelo> vueloExistente = repository.findByCodigoVuelo(dto.getCodigoVuelo());
        if (vueloExistente.isPresent()) {
            throw new BusinessException("El código de vuelo ya existe.");
        }

        LocalDate fechaIda = parseFecha(dto.getFechaIda());
        LocalDate fechaVuelta = parseFecha(dto.getFechaVuelta());
        validarFecha(fechaIda, fechaVuelta);

        Vuelo nuevoVuelo =crearVueloDesdeDTO(dto, fechaIda, fechaVuelta);

        Vuelo vueloGuardado = repository.save(nuevoVuelo);

        return modelMapper.map(vueloGuardado, VueloDTO.class);
    }

    @Override
    public VueloDTO actualizarVuelo(Long id, VueloDTO dto) {

        Vuelo vueloEncontrado = repository.findById(id).orElseThrow(() -> new BusinessException("Vuelo no encontrado con id: " + id));
        if (vueloEncontrado.isDeleted()) {
            throw new BusinessException("Vuelo con ID ha sido eliminado. " + id);
        }

        LocalDate fechaIda = parseFecha(dto.getFechaIda());
        LocalDate fechaVuelta = parseFecha(dto.getFechaVuelta());
        validarFecha(fechaIda, fechaVuelta);

        actualizarVueloDesdeDTO(vueloEncontrado, dto, fechaIda, fechaVuelta);
        Vuelo vueloGuardado = repository.save(vueloEncontrado);

        return modelMapper.map(vueloGuardado, VueloDTO.class);
    }

    @Override
    public List<VueloDTO> eliminar(Long id) {
        if (id <= 0) {
            throw new BusinessException("El id es obligatorio y debe ser numero positivo ");
        }

        Vuelo vueloBuscado = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Vuelo no encontrado con id: " + id));

        if (vueloBuscado.isDeleted()) {
            throw new BusinessException("El vuelo ya ha sido eliminado");
        }

        vueloBuscado.setDeleted(true);
        repository.save(vueloBuscado);

        List<Vuelo> listado = repository.findAllByDeletedFalse();
        return listado.stream().map(vuelo -> modelMapper.map(vuelo, VueloDTO.class))
                .toList();
    }

    @Override
    public VueloDTO buscarVueloPorId(Long id) {
        if (id <= 0) {
            throw new BusinessException("El id es obligatorio y debe ser numero positivo ");
        }
        Vuelo vueloBuscado = repository.findById(id).orElseThrow(() -> new BusinessException("El id proporcionado no encontrado: " + id));
        if (vueloBuscado.isDeleted()) {
            throw new BusinessException("El vuelo con ID ha sido eliminado");
        }
        return modelMapper.map(vueloBuscado, VueloDTO.class);
    }

    // Métodos Auxiliares
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
    private Vuelo crearVueloDesdeDTO(VueloDTO dto, LocalDate fechaIda, LocalDate fechaVuelta) {

        Vuelo vuelo = new Vuelo();
        vuelo.setCodigoVuelo(dto.getCodigoVuelo());
        vuelo.setOrigen(dto.getOrigen());
        vuelo.setDestino(dto.getDestino());
        vuelo.setTipoAsiento(dto.getTipoAsiento());
        vuelo.setPrecioPorPersona(dto.getPrecioPorPersona());
        vuelo.setFechaIda(fechaIda);
        vuelo.setFechaVuelta(fechaVuelta);
        return vuelo;
    }
    private void actualizarVueloDesdeDTO(Vuelo vuelo, VueloDTO dto, LocalDate fechaIda, LocalDate fechaVuelta) {
        vuelo.setCodigoVuelo(dto.getCodigoVuelo());
        vuelo.setOrigen(dto.getOrigen());
        vuelo.setDestino(dto.getDestino());
        vuelo.setTipoAsiento(dto.getTipoAsiento());
        vuelo.setPrecioPorPersona(dto.getPrecioPorPersona());
        vuelo.setFechaIda(fechaIda);
        vuelo.setFechaVuelta(fechaVuelta);
    }
    private LocalDate parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()){
            throw new BusinessException("La fecha no puede ser nula o vacía");
        }
        try {
            return LocalDate.parse(fechaStr);
        } catch (Exception e) {
            throw new BusinessException("Formato de fecha inválido. Utilice el siguiente formato dd/MM/yyyy.");
        }
    }
    private void validarFecha(LocalDate fechaIda, LocalDate fechaVuelta) {
        if (fechaIda.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha Ida no puede ser anterior a la fecha actual.");
        }
        if (fechaVuelta.isBefore(fechaIda)) {
            throw new BusinessException("La fecha de Vuelta no puede ser anterior a la fecha de ida.");
        }
        if (fechaVuelta.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de vuelta no puede ser anterior a la fecha actual. ");
        }
    }
}
