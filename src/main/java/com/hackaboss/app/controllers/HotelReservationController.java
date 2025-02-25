package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.ReservaHotelDTO;
import com.hackaboss.app.dtos.TotalMountDTO;
import com.hackaboss.app.services.IHotelReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agency/room-booking/new")
@RequiredArgsConstructor
public class HotelReservationController {

    private final IHotelReservationService service;

    @PostMapping
    public ResponseEntity<TotalMountDTO> createdReservation(@RequestBody ReservaHotelDTO reservaHotelDTO){
        TotalMountDTO reservation = service.createdReservation(reservaHotelDTO);
        return ResponseEntity.ok(reservation);
    }
}
