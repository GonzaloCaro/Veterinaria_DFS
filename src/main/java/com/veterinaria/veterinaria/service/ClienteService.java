package com.veterinaria.veterinaria.service;

import com.veterinaria.veterinaria.DTO.ClienteDTO;
import com.veterinaria.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria.exception.DuplicateResourceException;
import com.veterinaria.veterinaria.mapper.ClienteMapper;
import com.veterinaria.veterinaria.model.Cliente;
import com.veterinaria.veterinaria.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Transactional
    public ClienteDTO createCliente(ClienteDTO clienteDTO) {
        // Verificar si el correo ya existe
        if (clienteRepository.existsByCorreo(clienteDTO.getCorreo())) {
            throw new DuplicateResourceException("El correo ya está registrado");
        }

        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(savedCliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return clienteMapper.toDto(cliente);
    }

    @Transactional
    public ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        // Verificar si el correo ha cambiado y si ya existe
        if (!existingCliente.getCorreo().equals(clienteDTO.getCorreo())) {
            if (clienteRepository.existsByCorreo(clienteDTO.getCorreo())) {
                throw new DuplicateResourceException("El correo ya está registrado");
            }
        }

        existingCliente.setNombre(clienteDTO.getNombre());
        existingCliente.setTelefono(clienteDTO.getTelefono());
        existingCliente.setDireccion(clienteDTO.getDireccion());
        existingCliente.setCorreo(clienteDTO.getCorreo());

        Cliente updatedCliente = clienteRepository.save(existingCliente);
        return clienteMapper.toDto(updatedCliente);
    }

    @Transactional
    public void deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}