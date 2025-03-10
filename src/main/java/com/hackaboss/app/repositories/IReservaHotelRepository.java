package com.hackaboss.app.repositories;

import com.hackaboss.app.entities.ReservaHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IReservaHotelRepository extends JpaRepository<ReservaHotel, Long> {

}