package com.fiap.farmacheck.model.dto.estoque;

import com.fiap.farmacheck.model.dto.unidade.UnidadeRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstoqueRequestDTO(

        @NotNull(message = "A quantidade é obrigatória")
        Integer quantidade,

        @NotNull(message = "A unidade é obrigatória")
        int idUnidade,

        @NotNull(message = "O medicamento é obrigatório")
        int idMedicamento
) {
}
