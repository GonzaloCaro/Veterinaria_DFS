package com.veterinaria.veterinaria.mapper;

import com.veterinaria.veterinaria.DTO.FacturaServicioDTO;
import com.veterinaria.veterinaria.model.FacturaServicio;
import com.veterinaria.veterinaria.model.Factura;
import com.veterinaria.veterinaria.model.Servicio;
import org.springframework.stereotype.Component;

@Component
public class FacturaServicioMapper {

    public FacturaServicioDTO toDto(FacturaServicio facturaServicio) {
        FacturaServicioDTO dto = new FacturaServicioDTO();
        dto.setId(facturaServicio.getId());
        dto.setFacturaId(facturaServicio.getFactura().getId());
        dto.setServicioId(facturaServicio.getServicio().getId());
        dto.setCantidad(facturaServicio.getCantidad());
        dto.setPrecioUnitario(facturaServicio.getPrecioUnitario());
        return dto;
    }

    public FacturaServicio toEntity(FacturaServicioDTO dto, Factura factura, Servicio servicio) {
        FacturaServicio facturaServicio = new FacturaServicio();
        facturaServicio.setId(dto.getId());
        facturaServicio.setFactura(factura);
        facturaServicio.setServicio(servicio);
        facturaServicio.setCantidad(dto.getCantidad());
        facturaServicio.setPrecioUnitario(dto.getPrecioUnitario());
        return facturaServicio;
    }
}