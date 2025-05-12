package com.veterinaria.veterinaria.servicio;

import com.veterinaria.veterinaria.repository.ServicioRepository;
import com.veterinaria.veterinaria.mapper.ServicioMapper;
import com.veterinaria.veterinaria.service.ServicioService;
import com.veterinaria.veterinaria.DTO.ServicioDTO;
import com.veterinaria.veterinaria.model.Servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioServiceTest {

    private ServicioService servicioService;
    private ServicioRepository servicioRepository;
    private ServicioMapper servicioMapper;

    @BeforeEach
    public void setUp() {
        servicioRepository = mock(ServicioRepository.class);
        servicioMapper = mock(ServicioMapper.class); // Mockeamos el mapper
        servicioService = new ServicioService(servicioRepository, servicioMapper);
    }

    @Test
    public void testGetAllServicios() {
        // Datos de prueba
        Servicio servicio1 = new Servicio();
        servicio1.setNombre("Consulta General");

        Servicio servicio2 = new Servicio();
        servicio2.setNombre("Vacunación");

        List<Servicio> servicios = Arrays.asList(servicio1, servicio2);

        // Configurar mocks
        when(servicioRepository.findAll()).thenReturn(servicios);
        when(servicioMapper.toDto(any(Servicio.class)))
                .thenAnswer(invocation -> {
                    Servicio s = invocation.getArgument(0);
                    ServicioDTO dto = new ServicioDTO();
                    dto.setNombre(s.getNombre());
                    return dto;
                });

        // Llamar al método
        List<ServicioDTO> result = servicioService.getAllServicios();

        // Verificar
        assertEquals(2, result.size());
        assertEquals("Consulta General", result.get(0).getNombre());
        assertEquals("Vacunación", result.get(1).getNombre());
    }

    @Test
    public void testGetServicioById() {
        // Datos de prueba
        Servicio servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Consulta General");

        // Configurar mocks
        when(servicioRepository.findById(1L)).thenReturn(java.util.Optional.of(servicio));
        when(servicioMapper.toDto(any(Servicio.class)))
                .thenAnswer(invocation -> {
                    Servicio s = invocation.getArgument(0);
                    ServicioDTO dto = new ServicioDTO();
                    dto.setNombre(s.getNombre());
                    return dto;
                });

        // Llamar al método
        ServicioDTO result = servicioService.getServicioById(1L);

        // Verificar
        assertNotNull(result);
        assertEquals("Consulta General", result.getNombre());
    }

}
