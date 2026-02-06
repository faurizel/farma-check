package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.dto.auditoria.AuditoriaResponseDTO;
import com.fiap.farmacheck.model.entity.Auditoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditoriaMapper {

    default AuditoriaResponseDTO map(Auditoria auditoria) {
        if (auditoria == null) return null;

        return AuditoriaResponseDTO.builder()
                .criacao(auditoria.getCriacao())
                .ultimaModificacao(auditoria.getUltimaModificacao())
                .build();
    }
}
