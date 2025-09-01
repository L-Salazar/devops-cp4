package br.com.fiap.performancekids.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String cargo;
}
