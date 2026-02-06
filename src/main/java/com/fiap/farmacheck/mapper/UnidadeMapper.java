package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Unidade;
import com.fiap.farmacheck.model.dto.unidade.UnidadeRequestDTO;
import com.fiap.farmacheck.model.dto.unidade.UnidadeResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AuditoriaMapper.class})
public interface UnidadeMapper {

    // 1. CONVERSÃO (DTO -> Entity)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    Unidade toEntity(UnidadeRequestDTO dto);

    // 2. CONVERSÃO (Entity -> DTO)
    @Mapping(target = "auditoria", source = ".")
    UnidadeResponseDTO toResponse(Unidade entity);

    // 3. ATUALIZAÇÃO (DTO -> Entity Existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UnidadeRequestDTO dto, @MappingTarget Unidade unidade);
}