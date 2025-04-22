package com.veterinaria.veterinaria.repository;

import com.veterinaria.veterinaria.model.FacturaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaServicioRepository extends JpaRepository<FacturaServicio, Long> {
    List<FacturaServicio> findByFacturaId(Long facturaId);

    List<FacturaServicio> findByServicioId(Long servicioId);

    @Modifying
    @Query("DELETE FROM FacturaServicio fs WHERE fs.factura.id = :facturaId")
    void deleteByFacturaId(@Param("facturaId") Long facturaId);
}