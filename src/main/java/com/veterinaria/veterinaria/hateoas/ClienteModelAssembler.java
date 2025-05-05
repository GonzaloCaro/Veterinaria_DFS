package com.veterinaria.veterinaria.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.veterinaria.veterinaria.DTO.ClienteDTO;
import com.veterinaria.veterinaria.controller.ClienteController;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>> {

    @Override
    public EntityModel<ClienteDTO> toModel(ClienteDTO cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).getClienteById(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).getAllClientes()).withRel("all"),
                linkTo(methodOn(ClienteController.class).createCliente(cliente)).withRel("create"),
                linkTo(methodOn(ClienteController.class).updateCliente(cliente.getId(), cliente))
                        .withRel("update"),
                linkTo(methodOn(ClienteController.class).deleteCliente(cliente.getId())).withRel("delete"));
    }

}
