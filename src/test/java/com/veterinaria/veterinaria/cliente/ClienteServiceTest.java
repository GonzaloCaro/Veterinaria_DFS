package com.veterinaria.veterinaria.cliente;

import com.veterinaria.veterinaria.repository.ClienteRepository;
import com.veterinaria.veterinaria.mapper.ClienteMapper;
import com.veterinaria.veterinaria.service.ClienteService;
import com.veterinaria.veterinaria.DTO.ClienteDTO;
import com.veterinaria.veterinaria.model.Cliente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    private ClienteService clienteService;
    private ClienteRepository clienteRepository;
    private ClienteMapper clienteMapper;

    @BeforeEach
    public void setUp() {
        clienteRepository = mock(ClienteRepository.class);
        clienteMapper = mock(ClienteMapper.class); // Mockeamos el mapper
        clienteService = new ClienteService(clienteRepository, clienteMapper);
    }

    @Test
    public void testGetAllClientes() {
        // Datos de prueba
        Cliente cliente1 = new Cliente();
        cliente1.setNombre("Juan Perez");

        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Maria Lopez");

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        // Configurar mocks
        when(clienteRepository.findAll()).thenReturn(clientes);
        when(clienteMapper.toDto(any(Cliente.class)))
                .thenAnswer(invocation -> {
                    Cliente c = invocation.getArgument(0);
                    ClienteDTO dto = new ClienteDTO();
                    dto.setNombre(c.getNombre());
                    return dto;
                });

        // Llamar al método
        List<ClienteDTO> result = clienteService.getAllClientes();

        // Verificar
        assertEquals(2, result.size());
        assertEquals("Juan Perez", result.get(0).getNombre());
        assertEquals("Maria Lopez", result.get(1).getNombre());
    }

    @Test
    public void testGetClienteById() {
        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Perez");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setCorreo("juanperez@gmail.com");

        // Configurar mocks
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Configurar el comportamiento del mapper
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan Perez");
        clienteDTO.setTelefono("123456789");
        clienteDTO.setDireccion("Calle Falsa 123");
        clienteDTO.setCorreo("juanperez@gmail.com");

        when(clienteMapper.toDto(cliente)).thenReturn(clienteDTO);

        // Llamar al método a probar
        ClienteDTO result = clienteService.getClienteById(1L);

        // Verificar
        assertNotNull(result, "El resultado no debería ser null");
        assertEquals("Juan Perez", result.getNombre());
        assertEquals("123456789", result.getTelefono());

        // Verificar interacciones con los mocks
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteMapper, times(1)).toDto(cliente);
    }

}
