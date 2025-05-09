package com.veterinaria.veterinaria.repository;

import com.veterinaria.veterinaria.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    Optional<Servicio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
