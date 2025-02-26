package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agency")
public class HotelController {

    @Autowired
    HotelService service;

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
}
