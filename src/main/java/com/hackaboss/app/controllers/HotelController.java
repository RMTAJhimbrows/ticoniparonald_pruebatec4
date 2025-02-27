package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.services.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agency/hotels")
public class HotelController {

    @Autowired
    IHotelService service;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> getAllHotels(){
        List<HotelDTO> list = service.obtenerHoteles();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<HotelDTO>> getAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam(required = false) String destination
    ){
        List<HotelDTO> availableRooms = service.getAvailableRooms(destination, dateFrom, dateTo);
        return ResponseEntity.ok(availableRooms);
    }

    @PostMapping("/new")
    public ResponseEntity<HotelDTO> crearHotel(@RequestBody HotelDTO dto){
        return ResponseEntity.ok(service.guardarHotel(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<HotelDTO> actualizarHotel(@PathVariable Long id, @RequestBody HotelDTO dto){
        return ResponseEntity.ok(service.actualizarHotel(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<HotelDTO>> eliminarHotel(@PathVariable Long id){
        List<HotelDTO> listado = service.eliminarHotel(id);
        return ResponseEntity.ok(listado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> buscarHotelPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarHotelPorID(id));
    }
}
