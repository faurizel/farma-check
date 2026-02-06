package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Movimentacao;
import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoRequestDTO;
import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {UsuarioMapperHelper.class, EstoqueMapperHelper.class, AuditoriaMapper.class})
public interface MovimentacaoMapper {

    // 1. CONVERSÃO (DTO -> Entity)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "idUsuario", target = "usuario")
    @Mapping(source = "idEstoque", target = "estoque")
    Movimentacao toEntity(MovimentacaoRequestDTO dto);

    // 2. CONVERSÃO (Entity -> DTO)
    @Mapping(target = "auditoria", source = ".")
    MovimentacaoResponseDTO toResponse(Movimentacao movimentacao);
}