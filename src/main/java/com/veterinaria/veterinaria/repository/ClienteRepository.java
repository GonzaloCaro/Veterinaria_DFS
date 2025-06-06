package com.veterinaria.veterinaria.repository;

import com.veterinaria.veterinaria.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}
