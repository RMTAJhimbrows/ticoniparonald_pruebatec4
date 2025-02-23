package com.hackaboss.app.controllers;

import com.hackaboss.app.dtos.HotelDTO;
import com.hackaboss.app.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agency")
public class HotelController {

    @Autowired
    HotelService service;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTO>> getAllHotels(){
        List<HotelDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }
    
}
