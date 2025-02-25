package com.hackaboss.app.repositories;

import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.entities.ReservaHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IHotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findAllByDeletedFalse();
    Optional<Hotel> findByCodigoHotel(String codigoHotel);
}


