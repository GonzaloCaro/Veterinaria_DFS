package com.veterinaria.veterinaria.servicio;

import com.veterinaria.veterinaria.model.Servicio;
import com.veterinaria.veterinaria.repository.ServicioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServicioRepositoryTest {

    @Autowired
    private ServicioRepository servicioRepository;

    @Test
    public void testGuardarYBuscar() {
        Servicio servicio = new Servicio();
        servicio.setNombre("Vacunación");
        servicio.setPrecio(new java.math.BigDecimal("50.0"));

        Servicio saved = servicioRepository.save(servicio);

        Optional<Servicio> found = servicioRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Vacunación", found.get().getNombre());
    }
}
