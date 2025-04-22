package com.veterinaria.veterinaria.DTO;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FacturaDTO {

    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "El estado es obligatorio")
    private String estado;

    private List<FacturaServicioDTO> servicios;
    private BigDecimal total;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<FacturaServicioDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<FacturaServicioDTO> servicios) {
        this.servicios = servicios;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}