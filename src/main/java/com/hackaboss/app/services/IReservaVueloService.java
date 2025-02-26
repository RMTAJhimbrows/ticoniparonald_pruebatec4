package com.hackaboss.app.services;

import com.hackaboss.app.dtos.ReservaVueloDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.entities.ReservaVuelo;
import com.hackaboss.app.entities.Vuelo;

public interface IReservaVueloService {
    TotalMountDTO obtenerPrecioTotal(ReservaVueloDTO reservaVueloDTO);
    Double calcularPrecioTotal(ReservaVueloDTO dto, Vuelo vuelo);
    ReservaVuelo convertToEntity(ReservaVueloDTO dto, Vuelo vuelo);
}
