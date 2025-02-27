package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.VueloDTO;
import com.hackaboss.app.services.IVueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agency/flights")
public class VueloController {

    @Autowired
    IVueloService service;

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, List<VueloDTO>>> filtrarVuelo(
            @RequestParam (required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam (required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam (required = false)String origin,
            @RequestParam (required = false)String destination
            ){
        Map<String, List<VueloDTO>> vuelos = service.obtenerVuelosFiltrados(dateFrom, dateTo, origin, destination);
        return ResponseEntity.ok(vuelos);
    }

    @PostMapping("/new")
    public ResponseEntity<VueloDTO> crearVuelo(@RequestBody VueloDTO dto){
        return ResponseEntity.ok(service.guardarVuelo(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<VueloDTO> actualizarVuelo(@PathVariable Long id, @RequestBody VueloDTO vueloDTO){
        return ResponseEntity.ok(service.actualizarVuelo(id, vueloDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<VueloDTO>> eliminarVuelo(@PathVariable Long id){
        List<VueloDTO> listado = service.eliminar(id);
        return ResponseEntity.ok(listado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VueloDTO> buscarVueloPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarVueloPorId(id));
    }
}
