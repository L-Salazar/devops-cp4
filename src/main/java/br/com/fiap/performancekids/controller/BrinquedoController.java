package br.com.fiap.performancekids.controller;

import br.com.fiap.performancekids.dto.BrinquedoDTO;
import br.com.fiap.performancekids.entity.Brinquedo;
import br.com.fiap.performancekids.entity.Categoria;
import br.com.fiap.performancekids.service.BrinquedoService;
import br.com.fiap.performancekids.service.CategoriaService;
import br.com.fiap.performancekids.assembler.BrinquedoModelAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Brinquedos")
@RestController
@RequestMapping("/brinquedos")
public class BrinquedoController {

    @Autowired private BrinquedoService brinquedoService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private BrinquedoModelAssembler assembler;

    @Operation(summary = "Cadastra um brinquedo",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado",
                            content = @Content(schema = @Schema(implementation = BrinquedoDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            })
    @PostMapping
    public ResponseEntity<EntityModel<BrinquedoDTO>> cadastrar(@Valid @RequestBody BrinquedoDTO dto) {
        Brinquedo salvo = brinquedoService.salvar(fromDTO(dto));
        BrinquedoDTO out = toDTO(salvo);
        return ResponseEntity
                .created(URI.create("/brinquedos/" + out.getId()))
                .body(assembler.toModel(out));
    }

    @Operation(summary = "Lista todos os brinquedos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BrinquedoDTO.class))))
            })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<BrinquedoDTO>>> listar() {
        List<EntityModel<BrinquedoDTO>> list = brinquedoService.listarTodos()
                .stream().map(this::toDTO).map(assembler::toModel).toList();

        return ResponseEntity.ok(CollectionModel.of(
                list,
                Link.of("/brinquedos").withSelfRel()
        ));
    }

    @Operation(summary = "Busca brinquedo por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso",
                            content = @Content(schema = @Schema(implementation = BrinquedoDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BrinquedoDTO>> buscarPorId(@PathVariable Long id) {
        BrinquedoDTO dto = toDTO(brinquedoService.buscarPorId(id));
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @Operation(summary = "Altera um brinquedo por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso",
                            content = @Content(schema = @Schema(implementation = BrinquedoDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida"),
                    @ApiResponse(responseCode = "404", description = "Não encontrado")
            })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BrinquedoDTO>> alterar(@PathVariable Long id,
                                                             @Valid @RequestBody BrinquedoDTO dto) {
        Brinquedo atualizado = brinquedoService.atualizar(id, fromDTO(dto));
        return ResponseEntity.ok(assembler.toModel(toDTO(atualizado)));
    }

    @Operation(summary = "Altera parcialmente um brinquedo por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sucesso",
                            content = @Content(schema = @Schema(implementation = BrinquedoDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrado")
            })
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<BrinquedoDTO>> atualizarParcialmente(@PathVariable Long id,
                                                                           @RequestBody BrinquedoDTO dto) {
        Brinquedo brinquedoAtualizado = brinquedoService.atualizarParcial(id, fromDTO(dto));

        BrinquedoDTO brinquedoDTO = toDTO(brinquedoAtualizado);

        return ResponseEntity.ok(assembler.toModel(brinquedoDTO));
    }

    @Operation(summary = "Deleta um brinquedo por ID",
            responses = { @ApiResponse(responseCode = "204", description = "Sem conteúdo") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        brinquedoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ------- mapeadores DTO <-> Entidade (simples, locais) -------
    private Brinquedo fromDTO(BrinquedoDTO dto) {
        Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId());
        return Brinquedo.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .categoria(categoria)
                .classificacao(dto.getClassificacao())
                .tamanho(dto.getTamanho())
                .preco(dto.getPreco())
                .build();
    }

    private BrinquedoDTO toDTO(Brinquedo b) {
        return BrinquedoDTO.builder()
                .id(b.getId())
                .nome(b.getNome())
                .categoriaId(b.getCategoria().getId())
                .categoriaNome(b.getCategoria().getNome())
                .classificacao(b.getClassificacao())
                .tamanho(b.getTamanho())
                .preco(b.getPreco())
                .build();
    }
}
