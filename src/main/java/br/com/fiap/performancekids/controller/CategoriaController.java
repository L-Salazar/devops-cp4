package br.com.fiap.performancekids.controller;

import br.com.fiap.performancekids.entity.Categoria;
import br.com.fiap.performancekids.service.CategoriaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria) {
        Categoria salvo = service.salvar(categoria);
        return ResponseEntity.created(URI.create("/categorias/" + salvo.getId())).body(salvo);
    }

    @GetMapping
    public List<Categoria> listar() { return service.listarTodos(); }

    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Long id) { return service.buscarPorId(id); }

    @PutMapping("/{id}")
    public Categoria atualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        return service.atualizar(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) { service.deletar(id); }
}
