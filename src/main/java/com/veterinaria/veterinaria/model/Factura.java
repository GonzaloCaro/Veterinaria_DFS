package com.veterinaria.veterinaria.model;

import java.util.List;

public class Factura {
    private String id;
    private String clienteId;
    private List<Servicio> servicios;
    private String estado; // Pendiente, Pagada, Cancelada

    public Factura(String id, String clienteId, List<Servicio> servicios, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.servicios = servicios;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
