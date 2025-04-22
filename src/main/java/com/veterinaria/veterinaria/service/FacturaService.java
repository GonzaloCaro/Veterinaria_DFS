package com.veterinaria.veterinaria.service;

import com.veterinaria.veterinaria.DTO.*;
import com.veterinaria.veterinaria.exception.*;
import com.veterinaria.veterinaria.mapper.FacturaMapper;
import com.veterinaria.veterinaria.model.*;
import com.veterinaria.veterinaria.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final FacturaServicioRepository facturaServicioRepository;
    private final FacturaMapper facturaMapper;

    public FacturaService(FacturaRepository facturaRepository,
            ClienteRepository clienteRepository,
            ServicioRepository servicioRepository,
            FacturaMapper facturaMapper,
            FacturaServicioRepository facturaServicioRepository) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.facturaServicioRepository = facturaServicioRepository;
        this.facturaMapper = facturaMapper;
    }

    @Transactional
    public FacturaDTO createFactura(FacturaDTO facturaDTO) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(facturaDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        // Crear factura base
        Factura factura = facturaMapper.toEntity(facturaDTO, cliente);
        factura.setEstado("PENDIENTE"); // Estado inicial

        // Validar y agregar servicios
        if (facturaDTO.getServicios() == null || facturaDTO.getServicios().isEmpty()) {
            throw new BusinessException("La factura debe tener al menos un servicio");
        }

        List<FacturaServicio> servicios = facturaDTO.getServicios().stream()
                .map(fsDto -> {
                    Servicio servicio = servicioRepository.findById(fsDto.getServicioId())
                            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

                    FacturaServicio fs = facturaMapper.toFacturaServicioEntity(fsDto, factura, servicio);

                    // Si no se especifica precio, usar el del servicio
                    if (fs.getPrecioUnitario() == null) {
                        fs.setPrecioUnitario(servicio.getPrecio());
                    }

                    return fs;
                })
                .collect(Collectors.toList());

        factura.setServicios(servicios);

        Factura savedFactura = facturaRepository.save(factura);
        return facturaMapper.toDto(savedFactura);
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> getAllFacturas() {
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FacturaDTO getFacturaById(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada"));
        return facturaMapper.toDto(factura);
    }

    @Transactional
    public void deleteFactura(Long id) {
        // Verificar existencia
        if (!facturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Factura no encontrada con id: " + id);
        }

        // Eliminar servicios asociados primero
        facturaServicioRepository.deleteByFacturaId(id);

        // Luego eliminar la factura
        facturaRepository.deleteById(id);
    }

    @Transactional
    public FacturaDTO updateFacturaCompleta(Long id, FacturaDTO facturaDTO) {
        Factura existingFactura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id: " + id));

        // Validar cliente
        Cliente cliente = clienteRepository.findById(facturaDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        // Validar servicios
        if (facturaDTO.getServicios() == null || facturaDTO.getServicios().isEmpty()) {
            throw new BusinessException("La factura debe tener al menos un servicio");
        }

        // Actualizar campos básicos
        existingFactura.setCliente(cliente);
        existingFactura.setEstado(facturaDTO.getEstado());

        // Eliminar servicios existentes
        facturaServicioRepository.deleteByFacturaId(id);

        // Crear nuevos servicios
        List<FacturaServicio> nuevosServicios = facturaDTO.getServicios().stream()
                .map(fsDto -> {
                    Servicio servicio = servicioRepository.findById(fsDto.getServicioId())
                            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

                    FacturaServicio fs = new FacturaServicio();
                    fs.setFactura(existingFactura);
                    fs.setServicio(servicio);
                    fs.setCantidad(fsDto.getCantidad());
                    fs.setPrecioUnitario(
                            fsDto.getPrecioUnitario() != null ? fsDto.getPrecioUnitario() : servicio.getPrecio());
                    return fs;
                })
                .collect(Collectors.toList());

        existingFactura.setServicios(nuevosServicios);

        Factura updatedFactura = facturaRepository.save(existingFactura);
        return facturaMapper.toDto(updatedFactura);
    }
}