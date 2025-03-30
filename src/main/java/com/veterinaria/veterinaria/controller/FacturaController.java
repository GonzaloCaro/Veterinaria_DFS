package com.veterinaria.veterinaria.controller;

import com.veterinaria.veterinaria.model.Factura;
import com.veterinaria.veterinaria.service.FacturaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    // GET /facturas
    @GetMapping
    public List<Factura> getAllFacturas() {
        return facturaService.obtenerTodas();
    }

    // GET /facturas/1
    @GetMapping("/{id}")
    public Factura getFacturaById(@PathVariable String id) {
        return facturaService.obtenerPorId(id);
    }

    // GET /facturas/cliente/1
    @GetMapping("/cliente/{clienteId}")
    public List<Factura> getFacturasByCliente(@PathVariable String clienteId) {
        return facturaService.obtenerPorCliente(clienteId);
    }

}
