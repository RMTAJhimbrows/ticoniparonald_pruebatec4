package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.ReservaVueloDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.services.IReservaVueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agency/flight-booking/new")
public class ReservaVueloController {

    @Autowired
    IReservaVueloService service;
    @PostMapping
    public ResponseEntity<TotalMountDTO> reservaDeVuelo(@RequestBody ReservaVueloDTO reservaVueloDTO){
        return ResponseEntity.ok(service.obtenerPrecioTotal(reservaVueloDTO));
    }
}