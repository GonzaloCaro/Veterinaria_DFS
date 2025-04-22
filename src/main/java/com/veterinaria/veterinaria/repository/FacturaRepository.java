package com.veterinaria.veterinaria.repository;

import com.veterinaria.veterinaria.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByClienteId(Long clienteId);

    List<Factura> findByEstado(String estado);

    @Query("SELECT f FROM Factura f WHERE f.fechaCreacion BETWEEN :startDate AND :endDate")
    List<Factura> findByFechaBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}