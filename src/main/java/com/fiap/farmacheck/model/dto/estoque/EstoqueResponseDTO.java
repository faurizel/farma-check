package com.fiap.farmacheck.model.dto.estoque;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fiap.farmacheck.model.dto.auditoria.AuditoriaResponseDTO;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoResponseDTO;
import com.fiap.farmacheck.model.dto.unidade.UnidadeResponseDTO;

public record EstoqueResponseDTO(

        int id,

        Integer quantidade,

        UnidadeResponseDTO unidade,

        MedicamentoResponseDTO medicamento,

        @JsonUnwrapped
        AuditoriaResponseDTO auditoria
) {
}
