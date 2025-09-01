package br.com.fiap.performancekids.service;

import br.com.fiap.performancekids.entity.Brinquedo;
import br.com.fiap.performancekids.repository.BrinquedoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrinquedoService {

    @Autowired
    private BrinquedoRepository repository;

    public Brinquedo salvar(Brinquedo b) {
        validarRegras(b);
        return repository.save(b);
    }

    public List<Brinquedo> listarTodos() {
        return repository.findAll();
    }

    public Brinquedo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brinquedo com ID " + id + " não encontrado."));
    }

    // Atualização completa (PUT)
    public Brinquedo atualizar(Long id, Brinquedo atualizado) {
        Brinquedo existente = buscarPorId(id);
        // Atualiza os campos da entidade
        existente.setNome(atualizado.getNome());
        existente.setCategoria(atualizado.getCategoria());
        existente.setClassificacao(atualizado.getClassificacao());
        existente.setTamanho(atualizado.getTamanho());
        existente.setPreco(atualizado.getPreco());
        validarRegras(existente);
        return repository.save(existente);
    }

    // Atualização parcial (PATCH)
    public Brinquedo atualizarParcial(Long id, Brinquedo brinquedoAtualizado) {
        Brinquedo existente = buscarPorId(id);

        if (brinquedoAtualizado.getNome() != null) {
            existente.setNome(brinquedoAtualizado.getNome());
        }
        if (brinquedoAtualizado.getCategoria() != null) {
            existente.setCategoria(brinquedoAtualizado.getCategoria());
        }
        if (brinquedoAtualizado.getClassificacao() != null) {
            existente.setClassificacao(brinquedoAtualizado.getClassificacao());
        }
        if (brinquedoAtualizado.getTamanho() != null) {
            existente.setTamanho(brinquedoAtualizado.getTamanho());
        }
        if (brinquedoAtualizado.getPreco() != null) {
            existente.setPreco(brinquedoAtualizado.getPreco());
        }

        validarRegras(existente);

        return repository.save(existente);
    }

    // Deletar brinquedo
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Brinquedo com ID " + id + " não encontrado para exclusão.");
        }
        repository.deleteById(id);
    }

    private void validarRegras(Brinquedo b) {
        if (b.getCategoria() == null || b.getCategoria().getNome() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
        String tipo = b.getCategoria().getNome().toUpperCase();
        String tam  = b.getTamanho() == null ? "" : b.getTamanho().toUpperCase();

        switch (tipo) {
            case "BOLA":
                if (!tam.matches("3|4|5"))
                    throw new IllegalArgumentException("Para categoria BOLA, tamanho deve ser 3, 4 ou 5.");
                break;
            case "ROUPA":
            case "MEIA":
                if (!tam.matches("PP|P|M|G"))
                    throw new IllegalArgumentException("Para ROUPA/MEIA, tamanho deve ser PP, P, M ou G.");
                break;
            case "TENIS":
                if (!tam.matches("2[8-9]|3[0-6]"))
                    throw new IllegalArgumentException("Para TÊNIS, tamanho deve ser 28 a 36.");
                break;
            case "RAQUETE":
            case "ACESSORIO":
                // tamanhos livres (ex.: P/M/G/UN)
                break;
            default:
                throw new IllegalArgumentException("Categoria inválida. Use: BOLA, TENIS, MEIA, ROUPA, RAQUETE ou ACESSORIO.");
        }

        String cl = b.getClassificacao() == null ? "" : b.getClassificacao();
        if (!cl.matches("3-5|6-8|9-12")) {
            throw new IllegalArgumentException("Classificação deve ser 3-5, 6-8 ou 9-12.");
        }
    }
}
