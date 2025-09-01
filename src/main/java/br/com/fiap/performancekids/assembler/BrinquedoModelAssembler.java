package br.com.fiap.performancekids.assembler;

import br.com.fiap.performancekids.controller.BrinquedoController;
import br.com.fiap.performancekids.dto.BrinquedoDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BrinquedoModelAssembler implements RepresentationModelAssembler<BrinquedoDTO, EntityModel<BrinquedoDTO>> {

    @Override
    public EntityModel<BrinquedoDTO> toModel(BrinquedoDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(BrinquedoController.class).buscarPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(BrinquedoController.class).listar()).withRel("lista"),
                linkTo(methodOn(BrinquedoController.class).alterar(dto.getId(), dto)).withRel("atualizar"),
                linkTo(methodOn(BrinquedoController.class).deletar(dto.getId())).withRel("deletar")
        );
    }
}
