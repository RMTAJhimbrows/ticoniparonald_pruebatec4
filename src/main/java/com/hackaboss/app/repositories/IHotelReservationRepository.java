package com.hackaboss.app.repositories;

import com.hackaboss.app.entities.Hotel;
import com.hackaboss.app.entities.ReservaHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHotelReservationRepository extends JpaRepository<ReservaHotel, Long> {
    Optional<ReservaHotel> findByHotel_CodigoHotel(String codigoHotel);

}
