package com.fiap.farmacheck.model.dto.medicamento;

import jakarta.validation.constraints.NotBlank;

public record MedicamentoRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O principio ativo é obrigatório")
        String pricipioAtivo,

        @NotBlank(message = "A dosagem é obrigatória")
        String dosagem,

        @NotBlank(message = "O laboratório é obrigatório")
        String laboratorio
) {
}
