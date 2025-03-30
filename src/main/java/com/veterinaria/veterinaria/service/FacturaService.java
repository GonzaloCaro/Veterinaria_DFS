package com.veterinaria.veterinaria.service;

import com.veterinaria.veterinaria.model.Factura;
import com.veterinaria.veterinaria.model.Servicio;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaService {
    private final List<Factura> facturas = new ArrayList<>();

    public FacturaService() {
        // Datos en memoria
        List<Servicio> servicios1 = List.of(
                new Servicio("Consulta general", 50.0),
                new Servicio("Vacunación", 30.0));

        List<Servicio> servicios2 = List.of(
                new Servicio("Cirugía", 200.0),
                new Servicio("Hospitalización", 80.0));

        facturas.add(new Factura("1", "1", servicios1, "Pendiente"));
        facturas.add(new Factura("2", "2", servicios2, "Pagada"));
        facturas.add(new Factura("3", "1",
                List.of(new Servicio("Análisis de sangre", 45.0)), "Pendiente"));
    }

    public List<Factura> obtenerTodas() {
        return facturas;
    }

    public Factura obtenerPorId(String id) {
        return facturas.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Factura> obtenerPorCliente(String clienteId) {
        return facturas.stream()
                .filter(f -> f.getClienteId().equals(clienteId))
                .toList();
    }
}
