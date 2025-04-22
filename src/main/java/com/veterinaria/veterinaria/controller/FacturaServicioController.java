package com.veterinaria.veterinaria.controller;

import com.veterinaria.veterinaria.DTO.FacturaServicioDTO;
import com.veterinaria.veterinaria.model.ResponseWrapper;
import com.veterinaria.veterinaria.service.FacturaServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/facturas-servicios")
public class FacturaServicioController {

    private final FacturaServicioService facturaServicioService;

    public FacturaServicioController(FacturaServicioService facturaServicioService) {
        this.facturaServicioService = facturaServicioService;
    }

    @GetMapping
    public ResponseEntity<?> getAllFacturaServicios() {
        log.debug("Controller: Obteniendo todos los servicios de la factura");
        List<FacturaServicioDTO> facturaServicios = facturaServicioService.getAllFacturaServicios();
        if (facturaServicios.isEmpty()) {
            log.error("Controller: No se encontraron servicios de la factura");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron servicios de la factura");
        }
        log.debug("Controller: Se encontraron {} servicios de la factura", facturaServicios.size());
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        "OK",
                        facturaServicios.size(),
                        facturaServicios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacturaServicioById(@PathVariable Long id) {
        log.debug("Controller: Obteniendo servicio de la factura con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de servicio de la factura no puede ser nulo");
            return ResponseEntity.badRequest().body("ID de servicio de la factura no puede ser nulo");
        }
        try {
            log.debug("Controller: Buscando servicio de la factura con id: {}", id);
            FacturaServicioDTO facturaServicio = facturaServicioService.getFacturaServicioById(id);
            if (facturaServicio == null) {
                log.error("Controller: Servicio de la factura no encontrado con id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Servicio de la factura no encontrado con id: " + id);
            }
            log.debug("Controller: Servicio de la factura encontrado con id: {}", id);
            return ResponseEntity.ok(facturaServicio);
        } catch (Exception e) {
            log.error("Controller: Error al buscar el servicio de la factura con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el servicio de la factura");
        }

    }

    @PostMapping
    public ResponseEntity<?> createFacturaServicio(
            @RequestBody FacturaServicioDTO facturaServicioDTO) {
        log.debug("Controller: Creando nuevo servicio de la factura: {}", facturaServicioDTO);
        if (facturaServicioDTO == null) {
            log.error("Controller: Servicio de la factura no puede ser nulo");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Servicio de la factura no puede ser nulo");
        }
        try {
            log.debug("Controller: Creando servicio de la factura: {}", facturaServicioDTO);
            if (facturaServicioDTO.getId() != null) {
                log.error("Controller: ID de servicio de la factura no debe ser nulo al crear un nuevo servicio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID de servicio de la factura no debe ser nulo al crear un nuevo servicio");
            }
            log.debug("Controller: Creando servicio de la factura: {}", facturaServicioDTO);
            FacturaServicioDTO createdFacturaServicio = facturaServicioService
                    .createFacturaServicio(facturaServicioDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseWrapper<>(
                            "Servicio de la factura creado exitosamente",
                            1,
                            List.of(createdFacturaServicio)));
        } catch (Exception e) {
            log.error("Controller: Error al crear el servicio de la factura", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el servicio de la factura");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacturaServicio(@PathVariable Long id,
            @RequestBody FacturaServicioDTO facturaServicioDTO) {
        log.debug("Controller: Actualizando servicio de la factura con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de servicio de la factura no puede ser nulo");
            return ResponseEntity.badRequest().body("ID de servicio de la factura no puede ser nulo");
        }
        if (facturaServicioDTO == null) {
            log.error("Controller: Servicio de la factura no puede ser nulo");
            return ResponseEntity.badRequest().body("Servicio de la factura no puede ser nulo");
        }
        try {
            log.debug("Controller: Actualizando servicio de la factura con id: {}", id);
            if (!id.equals(facturaServicioDTO.getId())) {
                log.error(
                        "Controller: ID de servicio de la factura en la URL no coincide con el ID en el cuerpo de la solicitud");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID de servicio de la factura en la URL no coincide con el ID en el cuerpo de la solicitud");
            }
            FacturaServicioDTO updatedFacturaServicio = facturaServicioService.updateFacturaServicio(id,
                    facturaServicioDTO);
            log.debug("Controller: Servicio de la factura actualizado exitosamente: {}", updatedFacturaServicio);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Servicio de la factura actualizado exitosamente",
                            1,
                            List.of(updatedFacturaServicio)));
        } catch (Exception e) {
            log.error("Controller: Error al actualizar el servicio de la factura con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el servicio de la factura");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacturaServicio(@PathVariable Long id) {
        log.debug("Controller: Eliminando servicio de la factura con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de servicio de la factura no puede ser nulo");
            return ResponseEntity.badRequest().build();
        }
        try {
            log.debug("Controller: Buscando servicio de la factura con id: {}", id);
            FacturaServicioDTO existingFacturaServicio = facturaServicioService.getFacturaServicioById(id);
            if (existingFacturaServicio == null) {
                log.error("Controller: Servicio de la factura no encontrado con id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Servicio de la factura no encontrado con id: " + id);
            }
            facturaServicioService.deleteFacturaServicio(id);
            log.debug("Controller: Servicio de la factura eliminado exitosamente con id: {}", id);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Servicio de la factura eliminado exitosamente",
                            1,
                            List.of(existingFacturaServicio)));
        } catch (Exception e) {
            log.error("Controller: Error al buscar el servicio de la factura con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar el servicio de la factura");
        }

    }
}