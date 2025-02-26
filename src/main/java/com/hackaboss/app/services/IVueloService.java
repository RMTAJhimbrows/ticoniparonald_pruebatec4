package com.hackaboss.app.services;

import com.hackaboss.app.dtos.VueloDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IVueloService {
    Map<String, List<VueloDTO>> obtenerVuelos(LocalDate fechaIda, LocalDate fechaVuelta, String origen, String destino);
}
