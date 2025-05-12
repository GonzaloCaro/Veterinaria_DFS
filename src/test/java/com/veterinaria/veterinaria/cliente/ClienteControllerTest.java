package com.veterinaria.veterinaria.cliente;

import com.veterinaria.veterinaria.controller.ClienteController;
import com.veterinaria.veterinaria.service.ClienteService;
import com.veterinaria.veterinaria.DTO.ClienteDTO;
import com.veterinaria.veterinaria.hateoas.ClienteModelAssembler;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean

    private ClienteService clienteService;
    @SuppressWarnings("removal")
    @MockBean
    private ClienteModelAssembler clienteModelAssembler;

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })
    public void testGetClienteById() throws Exception {

        // Datos de prueba
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(1L);
        cliente.setNombre("Juan Perez");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setCorreo("juanp@gmail.com");

        EntityModel<ClienteDTO> clienteModel = EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).getClienteById(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).getAllClientes()).withRel("all"),
                linkTo(methodOn(ClienteController.class).createCliente(cliente)).withRel("create"),
                linkTo(methodOn(ClienteController.class).updateCliente(cliente.getId(), cliente))
                        .withRel("update"),
                linkTo(methodOn(ClienteController.class).deleteCliente(cliente.getId())).withRel("delete"));

        when(clienteService.getClienteById(cliente.getId())).thenReturn(cliente);
        when(clienteModelAssembler.toModel(cliente)).thenReturn(clienteModel);

        mockMvc.perform(get("/api/clientes/{id}", cliente.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.nombre").value(cliente.getNombre()))
                .andExpect(jsonPath("$.telefono").value(cliente.getTelefono()))
                .andExpect(jsonPath("$.direccion").value(cliente.getDireccion()))
                .andExpect(jsonPath("$.correo").value(cliente.getCorreo()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.all.href").exists())
                .andExpect(jsonPath("$._links.create.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })
    public void testCreateCliente() throws Exception {

        // Datos de prueba
        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombre("Juan Perez");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setCorreo("juanperez@gmail.com");

        ClienteDTO savedCliente = new ClienteDTO();
        savedCliente.setId(1L);
        savedCliente.setNombre(cliente.getNombre());
        savedCliente.setTelefono(cliente.getTelefono());
        savedCliente.setDireccion(cliente.getDireccion());
        savedCliente.setCorreo(cliente.getCorreo());

        when(clienteService.createCliente(any(ClienteDTO.class))).thenReturn(savedCliente);
        EntityModel<ClienteDTO> clienteModel = EntityModel.of(savedCliente,
                linkTo(methodOn(ClienteController.class).getClienteById(savedCliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).getAllClientes()).withRel("all"),
                linkTo(methodOn(ClienteController.class).createCliente(cliente)).withRel("create"),
                linkTo(methodOn(ClienteController.class).updateCliente(savedCliente.getId(), savedCliente))
                        .withRel("update"),
                linkTo(methodOn(ClienteController.class).deleteCliente(savedCliente.getId())).withRel("delete"));

        when(clienteModelAssembler.toModel(savedCliente)).thenReturn(clienteModel);

        mockMvc.perform(post("/api/clientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedCliente.getId()))
                .andExpect(jsonPath("$.nombre").value(savedCliente.getNombre()))
                .andExpect(jsonPath("$.telefono").value(savedCliente.getTelefono()))
                .andExpect(jsonPath("$.direccion").value(savedCliente.getDireccion()))
                .andExpect(jsonPath("$.correo").value(savedCliente.getCorreo()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.all.href").exists())
                .andExpect(jsonPath("$._links.create.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })
    public void testCreateCliente_ValidationFailed() throws Exception {

        // Datos de prueba
        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombre("");

        mockMvc.perform(post("/api/clientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());

    }

}
