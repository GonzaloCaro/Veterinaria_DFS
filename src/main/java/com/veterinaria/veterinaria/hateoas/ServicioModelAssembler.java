package com.veterinaria.veterinaria.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.veterinaria.veterinaria.DTO.ServicioDTO;
import com.veterinaria.veterinaria.controller.ServicioController;

@Component
public class ServicioModelAssembler implements RepresentationModelAssembler<ServicioDTO, EntityModel<ServicioDTO>> {

    @Override
    public EntityModel<ServicioDTO> toModel(ServicioDTO servicio) {
        return EntityModel.of(servicio,
                linkTo(methodOn(ServicioController.class).getServicioById(servicio.getId())).withSelfRel(),
                linkTo(methodOn(ServicioController.class).getAllServicios()).withRel("all"),
                linkTo(methodOn(ServicioController.class).createServicio(servicio)).withRel("create"),
                linkTo(methodOn(ServicioController.class).updateServicio(servicio.getId(), servicio))
                        .withRel("update"),
                linkTo(methodOn(ServicioController.class).deleteServicio(servicio.getId())).withRel("delete"));
    }

}
