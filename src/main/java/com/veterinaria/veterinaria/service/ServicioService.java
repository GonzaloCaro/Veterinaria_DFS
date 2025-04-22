package com.veterinaria.veterinaria.service;

import com.veterinaria.veterinaria.DTO.ServicioDTO;
import com.veterinaria.veterinaria.mapper.ServicioMapper;
import com.veterinaria.veterinaria.model.Servicio;
import com.veterinaria.veterinaria.repository.ServicioRepository;
import com.veterinaria.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria.exception.DuplicateResourceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;

    public ServicioService(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    @Transactional
    public ServicioDTO createServicio(ServicioDTO servicioDTO) {
        // Verificar si el nombre ya existe
        if (servicioRepository.existsByNombre(servicioDTO.getNombre())) {
            throw new DuplicateResourceException("El nombre del servicio ya está registrado");
        }

        Servicio servicio = servicioMapper.toEntity(servicioDTO);
        Servicio savedServicio = servicioRepository.save(servicio);
        return servicioMapper.toDto(savedServicio);
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> getAllServicios() {
        return servicioRepository.findAll().stream()
                .map(servicioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicioDTO getServicioById(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
        return servicioMapper.toDto(servicio);
    }

    @Transactional
    public ServicioDTO updateServicio(Long id, ServicioDTO servicioDTO) {
        Servicio existingServicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));

        // Verificar si el nombre ha cambiado y si ya existe
        if (!existingServicio.getNombre().equals(servicioDTO.getNombre())) {
            if (servicioRepository.existsByNombre(servicioDTO.getNombre())) {
                throw new DuplicateResourceException("El nombre del servicio ya está registrado");
            }
        }

        Servicio updatedServicio = servicioMapper.toEntity(servicioDTO);
        updatedServicio.setId(id); // Asegurarse de que el ID se mantenga
        Servicio savedServicio = servicioRepository.save(updatedServicio);
        return servicioMapper.toDto(savedServicio);
    }

    @Transactional
    public void deleteServicio(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Servicio no encontrado con id: " + id);
        }
        servicioRepository.deleteById(id);
    }
}
