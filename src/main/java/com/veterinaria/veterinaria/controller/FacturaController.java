package com.veterinaria.veterinaria.controller;

import com.veterinaria.veterinaria.DTO.FacturaDTO;
import com.veterinaria.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria.model.ResponseWrapper;
import com.veterinaria.veterinaria.service.FacturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<?> getAllFacturas() {
        log.debug("Controller: Obteniendo todas las facturas");
        List<FacturaDTO> facturas = facturaService.getAllFacturas();
        if (facturas.isEmpty()) {
            log.error("Controller: No se encontraron facturas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron facturas");
        }
        log.debug("Controller: Se encontraron {} facturas", facturas.size());
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        "OK",
                        facturas.size(),
                        facturas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacturaById(@PathVariable Long id) {
        log.debug("Controller: Obteniendo factura con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de factura no puede ser nulo");
            return ResponseEntity.badRequest().body("ID de factura no puede ser nulo");
        }
        try {
            log.debug("Controller: Buscando factura con id: {}", id);
            FacturaDTO factura = facturaService.getFacturaById(id);
            if (factura == null) {
                log.error("Controller: Factura no encontrada con id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Factura no encontrada con id: " + id);
            }
            log.debug("Controller: Factura encontrada con id: {}", id);
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            log.error("Controller: Error al buscar la factura con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la factura");
        }

    }

    @PostMapping
    public ResponseEntity<String> createFactura(@RequestBody FacturaDTO facturaDTO) {
        log.debug("Controller: Creando nueva factura");
        if (facturaDTO == null) {
            log.error("Controller: Factura no puede ser nula");
            return ResponseEntity.badRequest().body("Factura no puede ser nula");
        }
        try {
            log.debug("Controller: Factura a crear: {}", facturaDTO);
        } catch (Exception e) {
            log.error("Controller: Error al crear la factura", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la factura");
        }
        FacturaDTO createdFactura = facturaService.createFactura(facturaDTO);
        if (createdFactura == null) {
            log.error("Controller: Error al crear la factura");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la factura");
        }
        log.debug("Controller: Factura creada con id: {}", createdFactura.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseWrapper<>(
                        "Factura creada exitosamente",
                        1,
                        List.of(createdFactura)).toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompleteFactura(
            @PathVariable Long id,
            @RequestBody FacturaDTO facturaDTO) {
        log.debug("Controller: Actualizando factura con id: {}", id);
        try {
            FacturaDTO updatedFactura = facturaService.updateFacturaCompleta(id, facturaDTO);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Factura actualizada exitosamente",
                            1,
                            List.of(updatedFactura)));
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Factura no encontrada con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseWrapper<>("Error: " + e.getMessage(), 0, null));
        } catch (Exception e) {
            log.error("Controller: Error al actualizar la factura", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("Error al actualizar la factura", 0, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> forceDeleteFactura(@PathVariable Long id) {
        log.debug("Controller: Eliminando factura con id: {}", id);
        try {
            facturaService.deleteFactura(id);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Factura eliminada exitosamente",
                            1,
                            null));
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Factura no encontrada con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseWrapper<>("Error: " + e.getMessage(), 0, null));
        } catch (Exception e) {
            log.error("Controller: Error al eliminar la factura", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>("Error al eliminar la factura", 0, null));
        }
    }
}