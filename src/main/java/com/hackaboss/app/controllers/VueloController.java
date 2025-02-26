package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.VueloDTO;
import com.hackaboss.app.services.IVueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agency")
public class VueloController {

    @Autowired
    IVueloService service;


    @GetMapping("/flights")
    public ResponseEntity<Map<String, List<VueloDTO>>> filtrarVuelo(
            @RequestParam (required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam (required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam (required = false)String origin,
            @RequestParam (required = false)String destination
            ){
        Map<String, List<VueloDTO>> vuelos = service.obtenerVuelos(dateFrom, dateTo, origin, destination);
        return ResponseEntity.ok(vuelos);
    }
}
