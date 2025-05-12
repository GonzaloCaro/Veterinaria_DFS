package com.veterinaria.veterinaria.servicio;

import com.veterinaria.veterinaria.controller.ServicioController;
import com.veterinaria.veterinaria.service.ServicioService;
import com.veterinaria.veterinaria.DTO.ServicioDTO;
import com.veterinaria.veterinaria.hateoas.ServicioModelAssembler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@WebMvcTest(ServicioController.class)
public class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean

    private ServicioService servicioService;
    @SuppressWarnings("removal")
    @MockBean
    private ServicioModelAssembler servicioModelAssembler;

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })
    public void testGetServicioById() throws Exception {

        // Datos de prueba
        ServicioDTO servicio = new ServicioDTO();
        servicio.setId(1L);
        servicio.setNombre("Consulta");
        servicio.setPrecio(new java.math.BigDecimal("100.0"));

        EntityModel<ServicioDTO> servicioModel = EntityModel.of(servicio,
                linkTo(methodOn(ServicioController.class).getServicioById(servicio.getId())).withSelfRel(),
                linkTo(methodOn(ServicioController.class).getAllServicios()).withRel("all"),
                linkTo(methodOn(ServicioController.class).createServicio(servicio)).withRel("create"),
                linkTo(methodOn(ServicioController.class).updateServicio(servicio.getId(), servicio))
                        .withRel("update"),
                linkTo(methodOn(ServicioController.class).deleteServicio(servicio.getId())).withRel("delete"));

        when(servicioService.getServicioById(1L)).thenReturn(servicio);
        when(servicioModelAssembler.toModel(any(ServicioDTO.class))).thenReturn(servicioModel);

        mockMvc.perform(get("/api/servicios/{id}", servicio.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(servicio.getId()))
                .andExpect(jsonPath("$.nombre").value(servicio.getNombre()))
                .andExpect(jsonPath("$.precio").value(servicio.getPrecio()))
                .andExpect(jsonPath("$._links.self.href").value(linkTo(methodOn(ServicioController.class)
                        .getServicioById(servicio.getId())).toString()))
                .andExpect(jsonPath("$._links.all.href").value(linkTo(methodOn(ServicioController.class)
                        .getAllServicios()).toString()))
                .andExpect(jsonPath("$._links.create.href").value(linkTo(methodOn(ServicioController.class)
                        .createServicio(servicio)).toString()))
                .andExpect(jsonPath("$._links.update.href").value(linkTo(methodOn(ServicioController.class)
                        .updateServicio(servicio.getId(), servicio)).toString()))
                .andExpect(jsonPath("$._links.delete.href").value(linkTo(methodOn(ServicioController.class)
                        .deleteServicio(servicio.getId())).toString()));
    }

}
