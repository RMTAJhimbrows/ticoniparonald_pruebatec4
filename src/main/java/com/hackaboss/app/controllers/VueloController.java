package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.VueloDTO;
import com.hackaboss.app.entities.Vuelo;
import com.hackaboss.app.exceptions.ResourceNotFoundException;
import com.hackaboss.app.services.IVueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agency")

public class VueloController {

    @Autowired
    IVueloService service;

    @GetMapping("/flights")
    public ResponseEntity<List<VueloDTO>> obtenerVuelosRegistrados(){
        List<VueloDTO> vuelos = service.findAll();
        return ResponseEntity.ok(vuelos);
    }

}
