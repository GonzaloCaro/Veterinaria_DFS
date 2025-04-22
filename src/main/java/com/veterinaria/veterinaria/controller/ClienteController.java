package com.veterinaria.veterinaria.controller;

import com.veterinaria.veterinaria.DTO.ClienteDTO;
import com.veterinaria.veterinaria.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.veterinaria.veterinaria.exception.*;
import com.veterinaria.veterinaria.model.ResponseWrapper;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<?> getAllClientes() {
        log.debug("Controller: Obteniendo todos los clientes");
        List<ClienteDTO> clientes = clienteService.getAllClientes();
        if (clientes.isEmpty()) {
            log.error("Controller: No se encontraron clientes");
            throw new ResourceNotFoundException("No se encontraron clientes");
        }
        log.debug("Controller: Se encontraron {} clientes", clientes.size());
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        "OK",
                        clientes.size(),
                        clientes));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        log.debug("Controller: Obteniendo cliente con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de cliente no puede ser nulo");
            throw new IllegalArgumentException("ID de cliente no puede ser nulo");
        }
        try {
            log.debug("Controller: Buscando cliente con id: {}", id);
            ClienteDTO cliente = clienteService.getClienteById(id);
            if (cliente == null) {
                log.error("Controller: Cliente no encontrado con id: {}", id);
                throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
            }
            log.debug("Controller: Cliente encontrado: {}", cliente);
            return ResponseEntity.ok(cliente);
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Cliente no encontrado con id: {}", id);
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<ClienteDTO>> createCliente(@RequestBody ClienteDTO clienteDTO) {
        log.debug("Controller: Creando nuevo cliente: {}", clienteDTO);
        if (clienteDTO == null) {
            log.error("Controller: Cliente no puede ser nulo");
            throw new IllegalArgumentException("Cliente no puede ser nulo");
        }
        try {
            log.debug("Controller: Creando cliente: {}", clienteDTO);
            if (clienteDTO.getId() != null) {
                log.error("Controller: ID de cliente no debe ser nulo al crear un nuevo cliente");
                throw new IllegalArgumentException("ID de cliente no debe ser nulo al crear un nuevo cliente");
            }
            ClienteDTO createdCliente = clienteService.createCliente(clienteDTO);
            log.debug("Controller: Cliente creado exitosamente: {}", createdCliente);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseWrapper<>(
                            "Cliente creado exitosamente",
                            1,
                            List.of(createdCliente)));
        } catch (IllegalArgumentException e) {
            log.error("Controller: Error al crear cliente: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ClienteDTO>> updateCliente(@PathVariable Long id,
            @RequestBody ClienteDTO clienteDTO) {
        log.debug("Controller: Actualizando cliente con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de cliente no puede ser nulo");
            throw new IllegalArgumentException("ID de cliente no puede ser nulo");
        }
        if (clienteDTO == null) {
            log.error("Controller: Cliente no puede ser nulo");
            throw new IllegalArgumentException("Cliente no puede ser nulo");
        }

        try {
            log.debug("Controller: Actualizando cliente con id: {}", id);
            if (!id.equals(clienteDTO.getId())) {
                log.error("Controller: ID de cliente en la URL no coincide con el ID en el cuerpo de la solicitud");
                throw new IllegalArgumentException(
                        "ID de cliente en la URL no coincide con el ID en el cuerpo de la solicitud");
            }
            ClienteDTO updatedCliente = clienteService.updateCliente(id, clienteDTO);

            log.debug("Controller: Cliente actualizado exitosamente: {}", updatedCliente);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Cliente actualizado exitosamente",
                            1,
                            List.of(updatedCliente)));
        } catch (IllegalArgumentException e) {
            log.error("Controller: Error al actualizar cliente: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        log.debug("Controller: Eliminando cliente con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de cliente no puede ser nulo");
            throw new IllegalArgumentException("ID de cliente no puede ser nulo");
        }
        try {
            log.debug("Controller: Eliminando cliente con id: {}", id);
            clienteService.getClienteById(id); // Verificar si el cliente existe
            log.debug("Controller: Cliente encontrado con id: {}", id);
            clienteService.deleteCliente(id);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Cliente eliminado exitosamente",
                            0,
                            null));

        } catch (ResourceNotFoundException e) {
            log.error("Controller: Cliente no encontrado con id: {}", id);
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }
}