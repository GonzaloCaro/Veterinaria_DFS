package com.veterinaria.veterinaria.controller;

import com.veterinaria.veterinaria.DTO.ServicioDTO;
import com.veterinaria.veterinaria.service.ServicioService;
import com.veterinaria.veterinaria.exception.*;
import com.veterinaria.veterinaria.model.ResponseWrapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<?> getAllServicios() {
        log.debug("Controller: Obteniendo todos los servicios");
        List<ServicioDTO> servicios = servicioService.getAllServicios();
        if (servicios.isEmpty()) {
            log.error("Controller: No se encontraron servicios");
            throw new ResourceNotFoundException("No se encontraron servicios");
        }
        log.debug("Controller: Se encontraron {} servicios", servicios.size());
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        "OK",
                        servicios.size(),
                        servicios));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> getServicioById(@PathVariable Long id) {
        log.debug("Controller: Obteniendo servicio con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de servicio no puede ser nulo");
            throw new IllegalArgumentException("ID de servicio no puede ser nulo");
        }
        try {
            log.debug("Controller: Buscando servicio con id: {}", id);
            ServicioDTO servicio = servicioService.getServicioById(id);
            if (servicio == null) {
                log.error("Controller: Servicio no encontrado con id: {}", id);
                throw new ResourceNotFoundException("Servicio no encontrado con id: " + id);
            }
            log.debug("Controller: Servicio encontrado: {}", servicio);
            return ResponseEntity.ok(servicio);
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Servicio no encontrado con id: {}", id);
            throw e;
        }

    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<ServicioDTO>> createServicio(@RequestBody ServicioDTO servicioDTO) {
        log.debug("Controller: Creando nuevo servicio");
        if (servicioDTO == null) {
            log.error("Controller: Servicio no puede ser nulo");
            throw new IllegalArgumentException("Servicio no puede ser nulo");
        }
        try {
            log.debug("Controller: Creando servicio: {}", servicioDTO);
            if (servicioDTO.getId() != null) {
                log.error("Controller: ID de servicio no debe ser proporcionado al crear un nuevo servicio");
                throw new IllegalArgumentException(
                        "ID de servicio no debe ser proporcionado al crear un nuevo servicio");
            }
            log.debug("Controller: Creando servicio: {}", servicioDTO);
            ServicioDTO createdServicio = servicioService.createServicio(servicioDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseWrapper<>(
                            "Servicio creado exitosamente",
                            1,
                            List.of(createdServicio)));
        } catch (IllegalArgumentException e) {
            log.error("Controller: Error al crear servicio: {}", e.getMessage());
            throw e;
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ServicioDTO>> updateServicio(@PathVariable Long id,
            @RequestBody ServicioDTO servicioDTO) {
        log.debug("Controller: Actualizando servicio con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de servicio no puede ser nulo");
            throw new IllegalArgumentException("ID de servicio no puede ser nulo");
        }
        if (servicioDTO == null) {
            log.error("Controller: Servicio no puede ser nulo");
            throw new IllegalArgumentException("Servicio no puede ser nulo");
        }
        if (servicioDTO.getId() == null) {
            log.error("Controller: ID de servicio no puede ser nulo al actualizar un servicio");
            throw new IllegalArgumentException("ID de servicio no puede ser nulo al actualizar un servicio");
        }
        try {
            log.debug("Controller: Buscando servicio con id: {}", id);
            ServicioDTO existingServicio = servicioService.getServicioById(id);
            if (existingServicio == null) {
                log.error("Controller: Servicio no encontrado con id: {}", id);
                throw new ResourceNotFoundException("Servicio no encontrado con id: " + id);
            }
            log.debug("Controller: Actualizando servicio con id: {}", id);
            ServicioDTO updatedServicio = servicioService.updateServicio(id, servicioDTO);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Servicio actualizado exitosamente",
                            1,
                            List.of(updatedServicio)));
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Servicio no encontrado con id: {}", id);
            throw e;
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteServicio(@PathVariable Long id) {
        log.debug("Controller: Eliminando servicio con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de servicio no puede ser nulo");
            throw new IllegalArgumentException("ID de servicio no puede ser nulo");
        }
        try {
            log.debug("Controller: Buscando servicio con id: {}", id);
            ServicioDTO existingServicio = servicioService.getServicioById(id);
            if (existingServicio == null) {
                log.error("Controller: Servicio no encontrado con id: {}", id);
                throw new ResourceNotFoundException("Servicio no encontrado con id: " + id);
            }
            log.debug("Controller: Eliminando servicio con id: {}", id);
            servicioService.deleteServicio(id);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Servicio eliminado exitosamente",
                            0,
                            null));

        } catch (ResourceNotFoundException e) {
            log.error("Controller: Servicio no encontrado con id: {}", id);
            throw e;
        }

    }
}
