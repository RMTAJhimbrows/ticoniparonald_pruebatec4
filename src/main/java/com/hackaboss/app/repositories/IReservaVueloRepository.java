package com.hackaboss.app.repositories;

import com.hackaboss.app.entities.ReservaVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservaVueloRepository extends JpaRepository<ReservaVuelo, Long> {
}