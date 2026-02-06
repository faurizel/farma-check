package com.fiap.farmacheck.model.dto.unidade;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fiap.farmacheck.model.dto.auditoria.AuditoriaResponseDTO;
import com.fiap.farmacheck.model.enums.TipoUnidade;

public record UnidadeResponseDTO(

        int id,

        String nome,

        String cep,

        String telefone,

        String email,

        TipoUnidade tipoUnidade,

        @JsonUnwrapped
        AuditoriaResponseDTO auditoria
) {
}
