package com.hackaboss.app.services;

import com.hackaboss.app.dtos.VueloDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IVueloService {
    Map<String, List<VueloDTO>> obtenerVuelosFiltrados(LocalDate fechaIda, LocalDate fechaVuelta, String origen, String destino);

    List<VueloDTO> eliminar(Long id);

    VueloDTO buscarVueloPorId(Long id);

    VueloDTO actualizarVuelo(Long id, VueloDTO vueloDTO);

    VueloDTO guardarVuelo(VueloDTO dto);
}
