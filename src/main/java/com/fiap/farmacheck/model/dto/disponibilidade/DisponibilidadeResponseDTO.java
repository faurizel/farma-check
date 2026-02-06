package com.fiap.farmacheck.model.dto.disponibilidade;

public record DisponibilidadeResponseDTO(

        String nomeMedicamento,

        boolean disponivel,

        String mensagem
) {
}
