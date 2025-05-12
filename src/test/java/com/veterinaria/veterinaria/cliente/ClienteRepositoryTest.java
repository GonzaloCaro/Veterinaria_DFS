package com.veterinaria.veterinaria.cliente;

import com.veterinaria.veterinaria.model.Cliente;
import com.veterinaria.veterinaria.repository.ClienteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteRepositoryTest {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testGuardarYBuscar() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Perez");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setCorreo("juanp@gmail.com");
        
        Cliente saved = clienteRepository.save(cliente);

        Optional<Cliente> found = clienteRepository.findById(saved.getId()); // Usa el ID generado

        assertTrue(found.isPresent());
        assertEquals("Juan Perez", found.get().getNombre());
    }
}
