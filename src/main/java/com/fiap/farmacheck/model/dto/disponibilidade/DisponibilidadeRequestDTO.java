package com.fiap.farmacheck.model.dto.disponibilidade;

import jakarta.validation.constraints.NotBlank;

public record DisponibilidadeRequestDTO(

        @NotBlank(message = "O nome do medicamento é obrigatório")
        String nomeMedicamento
) {
}
