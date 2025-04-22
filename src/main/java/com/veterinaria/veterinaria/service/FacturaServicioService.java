package com.veterinaria.veterinaria.service;

import com.veterinaria.veterinaria.DTO.FacturaServicioDTO;
import com.veterinaria.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria.mapper.FacturaServicioMapper;
import com.veterinaria.veterinaria.model.*;
import com.veterinaria.veterinaria.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaServicioService {

    private final FacturaServicioRepository facturaServicioRepository;
    private final FacturaRepository facturaRepository;
    private final ServicioRepository servicioRepository;
    private final FacturaServicioMapper facturaServicioMapper;

    public FacturaServicioService(FacturaServicioRepository facturaServicioRepository,
            FacturaRepository facturaRepository,
            ServicioRepository servicioRepository,
            FacturaServicioMapper facturaServicioMapper) {
        this.facturaServicioRepository = facturaServicioRepository;
        this.facturaRepository = facturaRepository;
        this.servicioRepository = servicioRepository;
        this.facturaServicioMapper = facturaServicioMapper;
    }

    @Transactional
    public FacturaServicioDTO createFacturaServicio(FacturaServicioDTO facturaServicioDTO) {
        Factura factura = facturaRepository.findById(facturaServicioDTO.getFacturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada"));

        Servicio servicio = servicioRepository.findById(facturaServicioDTO.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        FacturaServicio facturaServicio = facturaServicioMapper.toEntity(facturaServicioDTO, factura, servicio);

        // Si no se especifica precio, usar el del servicio
        if (facturaServicio.getPrecioUnitario() == null) {
            facturaServicio.setPrecioUnitario(servicio.getPrecio());
        }

        FacturaServicio savedFacturaServicio = facturaServicioRepository.save(facturaServicio);
        return facturaServicioMapper.toDto(savedFacturaServicio);
    }

    @Transactional(readOnly = true)
    public List<FacturaServicioDTO> getAllFacturaServicios() {
        return facturaServicioRepository.findAll().stream()
                .map(facturaServicioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FacturaServicioDTO getFacturaServicioById(Long id) {
        FacturaServicio facturaServicio = facturaServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relación Factura-Servicio no encontrada"));
        return facturaServicioMapper.toDto(facturaServicio);
    }

    @Transactional
    public FacturaServicioDTO updateFacturaServicio(Long id, FacturaServicioDTO facturaServicioDTO) {
        FacturaServicio existingFacturaServicio = facturaServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relación Factura-Servicio no encontrada"));

        // Verificar si la factura está en estado editable
        if (!"PENDIENTE".equals(existingFacturaServicio.getFactura().getEstado())) {
            throw new IllegalStateException("No se puede modificar un servicio en una factura " +
                    existingFacturaServicio.getFactura().getEstado());
        }

        existingFacturaServicio.setCantidad(facturaServicioDTO.getCantidad());

        // Solo actualizar precio si se especifica
        if (facturaServicioDTO.getPrecioUnitario() != null) {
            existingFacturaServicio.setPrecioUnitario(facturaServicioDTO.getPrecioUnitario());
        }

        FacturaServicio updatedFacturaServicio = facturaServicioRepository.save(existingFacturaServicio);
        return facturaServicioMapper.toDto(updatedFacturaServicio);
    }

    @Transactional
    public void deleteFacturaServicio(Long id) {
        FacturaServicio facturaServicio = facturaServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relación Factura-Servicio no encontrada"));

        // Verificar si la factura está en estado editable
        if (!"PENDIENTE".equals(facturaServicio.getFactura().getEstado())) {
            throw new IllegalStateException("No se puede eliminar un servicio de una factura " +
                    facturaServicio.getFactura().getEstado());
        }

        facturaServicioRepository.delete(facturaServicio);
    }
}