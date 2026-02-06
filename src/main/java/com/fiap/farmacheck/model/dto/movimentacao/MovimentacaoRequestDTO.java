package com.fiap.farmacheck.model.dto.movimentacao;

import com.fiap.farmacheck.model.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;

public record MovimentacaoRequestDTO(

        @NotBlank(message = "A quantidade é obrigatória")
        Integer quantidade,

        @NotBlank(message = "O tipo de movimentacao é obrigatório")
        TipoMovimentacao tipoMovimentacao, // ENTRADA, SAIDA OU AJUSTE

        @NotBlank(message = "O usuário é obrigatório")
        int idUsuario,

        @NotBlank(message = "O estoque é obrigatório")
        int idEstoque
) {
}
