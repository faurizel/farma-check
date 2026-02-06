package com.fiap.farmacheck.model.dto.medicamento;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fiap.farmacheck.model.dto.auditoria.AuditoriaResponseDTO;

public record MedicamentoResponseDTO(

        int id,

        String nome,

        String pricipioAtivo,

        String dosagem,

        String laboratorio,

        @JsonUnwrapped
        AuditoriaResponseDTO auditoria
) {
}
