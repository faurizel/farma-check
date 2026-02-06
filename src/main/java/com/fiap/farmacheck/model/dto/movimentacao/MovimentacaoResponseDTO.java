package com.fiap.farmacheck.model.dto.movimentacao;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fiap.farmacheck.model.dto.auditoria.AuditoriaResponseDTO;
import com.fiap.farmacheck.model.dto.estoque.EstoqueResponseDTO;
import com.fiap.farmacheck.model.dto.usuario.UsuarioResponseDTO;
import com.fiap.farmacheck.model.enums.TipoMovimentacao;

public record MovimentacaoResponseDTO(

        int id,

        Integer quantidade,

        TipoMovimentacao tipoMovimentacao,

        UsuarioResponseDTO usuario,

        EstoqueResponseDTO estoque,

        @JsonUnwrapped
        AuditoriaResponseDTO auditoria
) {
}
