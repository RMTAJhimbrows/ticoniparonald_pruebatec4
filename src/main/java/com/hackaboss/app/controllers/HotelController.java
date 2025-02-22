package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.services.IHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agency/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final IHotelService service;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels(){
        return ResponseEntity.ok(service.getAllHotels());
    }

}
