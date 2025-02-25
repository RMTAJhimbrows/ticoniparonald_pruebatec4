package com.hackaboss.app.services;

import com.hackaboss.app.dtos.VueloDTO;

import java.util.List;

public interface IVueloService {
    List<VueloDTO> findAll();
}
