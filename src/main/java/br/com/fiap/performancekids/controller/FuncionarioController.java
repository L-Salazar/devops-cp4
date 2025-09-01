package br.com.fiap.performancekids.controller;

import br.com.fiap.performancekids.dto.FuncionarioDTO;
import br.com.fiap.performancekids.entity.Funcionario;
import br.com.fiap.performancekids.service.FuncionarioService;
import br.com.fiap.performancekids.service.ResourceNotFoundException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService service;

    @PostMapping
    public ResponseEntity<FuncionarioDTO> criar(@Valid @RequestBody Funcionario funcionario) {
        Funcionario salvo = service.salvar(funcionario);
        return ResponseEntity.created(URI.create("/funcionarios/" + salvo.getId()))
                .body(toDTO(salvo));
    }

    @GetMapping
    public List<FuncionarioDTO> listar() {
        return service.listarTodos().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public FuncionarioDTO buscarPorId(@PathVariable Long id) {
        return toDTO(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public FuncionarioDTO atualizar(@PathVariable Long id, @Valid @RequestBody Funcionario funcionario) {
        return toDTO(service.atualizar(id, funcionario));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    // PATCH parcial (não expõe senha)
    @PatchMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizarParcialmente(@PathVariable Long id,
                                                                @RequestBody FuncionarioDTO dto) {
        try {
            Funcionario existente = service.buscarPorId(id);

            if (dto.getNome() != null)  existente.setNome(dto.getNome());
            if (dto.getEmail() != null) existente.setEmail(dto.getEmail());
            if (dto.getCargo() != null) existente.setCargo(dto.getCargo());

            Funcionario atualizado = service.salvar(existente);
            return ResponseEntity.ok(toDTO(atualizado));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // mapper local
    private FuncionarioDTO toDTO(Funcionario f) {
        return FuncionarioDTO.builder()
                .id(f.getId())
                .nome(f.getNome())
                .email(f.getEmail())
                .cargo(f.getCargo())
                .build();
    }
}
