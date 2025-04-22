package com.veterinaria.veterinaria.mapper;

import com.veterinaria.veterinaria.DTO.ServicioDTO;
import com.veterinaria.veterinaria.model.Servicio;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {

    public ServicioDTO toDto(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(servicio.getId());
        dto.setNombre(servicio.getNombre());
        dto.setPrecio(servicio.getPrecio());
        return dto;
    }

    public Servicio toEntity(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setId(dto.getId());
        servicio.setNombre(dto.getNombre());
        servicio.setPrecio(dto.getPrecio());
        return servicio;
    }
}
