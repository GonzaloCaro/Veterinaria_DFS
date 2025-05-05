package com.veterinaria.veterinaria.controller;

import com.veterinaria.veterinaria.DTO.ClienteDTO;
import com.veterinaria.veterinaria.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.veterinaria.veterinaria.exception.*;
import com.veterinaria.veterinaria.model.ResponseWrapper;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

//

import com.veterinaria.veterinaria.hateoas.ClienteModelAssembler;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteModelAssembler clienteModelAssembler;

    public ClienteController(ClienteService clienteService,
            ClienteModelAssembler clienteModelAssembler) {
        this.clienteService = clienteService;
        this.clienteModelAssembler = clienteModelAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> getAllClientes() {

        log.debug("Controller: Obteniendo todos los clientes");

        List<ClienteDTO> clientes = clienteService.getAllClientes();

        if (clientes.isEmpty()) {
            log.error("Controller: No se encontraron clientes");
            throw new ResourceNotFoundException("No se encontraron clientes");
        }

        log.debug("Controller: Se encontraron {} clientes", clientes.size());

        List<EntityModel<ClienteDTO>> clienteModels = clientes.stream()
                .map(clienteModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(clienteModels,
                        linkTo(methodOn(ClienteController.class).getAllClientes()).withSelfRel()));

    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteDTO>> getClienteById(@PathVariable Long id) {

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

            return ResponseEntity.ok(clienteModelAssembler.toModel(cliente));

        } catch (ResourceNotFoundException e) {

            log.error("Controller: Cliente no encontrado con id: {}", id);

            throw e; // Será manejado por GlobalExceptionHandler
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<ClienteDTO>> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
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
                    .body(clienteModelAssembler.toModel(createdCliente));

        } catch (IllegalArgumentException e) {
            log.error("Controller: Error de validación al crear cliente: {}", e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            log.error("Controller: Error al crear cliente: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteDTO>> updateCliente(@Valid @PathVariable Long id,
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

            return ResponseEntity.ok(clienteModelAssembler.toModel(updatedCliente));
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