package com.fiap.farmacheck.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fiap.farmacheck.model.dto.auditoria.AuditoriaResponseDTO;
import com.fiap.farmacheck.model.dto.unidade.UnidadeResponseDTO;
import com.fiap.farmacheck.model.enums.TipoUsuario;

public record UsuarioResponseDTO(

        int id,

        String nome,

        String email,

        TipoUsuario tipoUsuario,

        UnidadeResponseDTO unidade,

        @JsonUnwrapped
        AuditoriaResponseDTO auditoria
) {
}