package com.hackaboss.app.repositories;

import com.hackaboss.app.entities.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVueloRepository extends JpaRepository<Vuelo, Long> {
    List<Vuelo> findAllByDeletedFalse();
}
