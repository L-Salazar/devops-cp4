package br.com.fiap.performancekids.dto;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrinquedoDTO {
    private Long id;
    private String nome;
    private Long categoriaId;
    private String categoriaNome;
    private String classificacao;
    private String tamanho;
    private BigDecimal preco;
}
