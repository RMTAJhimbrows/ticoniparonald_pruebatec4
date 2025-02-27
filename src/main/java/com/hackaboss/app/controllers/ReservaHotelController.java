package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.dtos.ReservaHotelDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.services.IReservaHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agency")
public class ReservaHotelController {

    @Autowired
    IReservaHotelService service;

    @GetMapping("/rooms")
    public ResponseEntity<List<HotelDTO>> getAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam(required = false) String destination
    ){
        List<HotelDTO> availableRooms = service.obtenerHabitacionesDisponibles(destination, dateFrom, dateTo);
        return ResponseEntity.ok(availableRooms);
    }

    @PostMapping("/room-booking/new")
    public ResponseEntity<TotalMountDTO> createdReservation(@RequestBody ReservaHotelDTO reservaHotelDTO){
        TotalMountDTO reservation = service.createdReservation(reservaHotelDTO);
        return ResponseEntity.ok(reservation);
    }
}
