package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Medicamento;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoRequestDTO;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {AuditoriaMapper.class})
public interface MedicamentoMapper {

    // 1. CONVERSÃO (DTO -> Entity)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    Medicamento toEntity(MedicamentoRequestDTO dto);

    // 2. CONVERSÃO (Entity -> DTO)
    @Mapping(target = "auditoria", source = ".")
    MedicamentoResponseDTO toResponse(Medicamento entity);

    // 3. ATUALIZAÇÃO (DTO -> Entity Existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(MedicamentoRequestDTO dto, @MappingTarget Medicamento medicamento);
}