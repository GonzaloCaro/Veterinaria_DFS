package com.veterinaria.veterinaria.mapper;

import com.veterinaria.veterinaria.DTO.FacturaDTO;
import com.veterinaria.veterinaria.DTO.FacturaServicioDTO;
import com.veterinaria.veterinaria.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacturaMapper {

    public FacturaDTO toDto(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setClienteId(factura.getCliente().getId());
        dto.setFechaCreacion(factura.getFechaCreacion());
        dto.setEstado(factura.getEstado());

        // Mapear servicios y calcular total
        if (factura.getServicios() != null) {
            List<FacturaServicioDTO> serviciosDto = factura.getServicios().stream()
                    .map(this::toFacturaServicioDto)
                    .collect(Collectors.toList());

            dto.setServicios(serviciosDto);

            BigDecimal total = factura.getServicios().stream()
                    .map(fs -> fs.getPrecioUnitario().multiply(BigDecimal.valueOf(fs.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            dto.setTotal(total);
        }

        return dto;
    }

    public Factura toEntity(FacturaDTO dto, Cliente cliente) {
        Factura factura = new Factura();
        factura.setId(dto.getId());
        factura.setCliente(cliente);
        factura.setFechaCreacion(dto.getFechaCreacion());
        factura.setEstado(dto.getEstado());
        return factura;
    }

    public FacturaServicioDTO toFacturaServicioDto(FacturaServicio facturaServicio) {
        FacturaServicioDTO dto = new FacturaServicioDTO();
        dto.setId(facturaServicio.getId());
        dto.setServicioId(facturaServicio.getServicio().getId());
        dto.setCantidad(facturaServicio.getCantidad());
        dto.setPrecioUnitario(facturaServicio.getPrecioUnitario());
        return dto;
    }

    public FacturaServicio toFacturaServicioEntity(FacturaServicioDTO dto, Factura factura, Servicio servicio) {
        FacturaServicio fs = new FacturaServicio();
        fs.setId(dto.getId());
        fs.setFactura(factura);
        fs.setServicio(servicio);
        fs.setCantidad(dto.getCantidad());
        fs.setPrecioUnitario(dto.getPrecioUnitario());
        return fs;
    }
}