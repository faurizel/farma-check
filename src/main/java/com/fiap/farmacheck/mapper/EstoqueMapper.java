package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.model.dto.estoque.EstoqueRequestDTO;
import com.fiap.farmacheck.model.dto.estoque.EstoqueResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = {UnidadeMapperHelper.class, MedicamentoMapperHelper.class, AuditoriaMapper.class})
public interface EstoqueMapper {

    // 1. CONVERSÃO (DTO -> Entity)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "idUnidade", target = "unidade")
    @Mapping(source = "idMedicamento", target = "medicamento")
    Estoque toEntity(EstoqueRequestDTO dto);

    // 2. CONVERSÃO (Entity -> DTO)
    @Mapping(target = "unidade", source = "unidade")
    EstoqueResponseDTO toResponse(Estoque entity);

    // 3. ATUALIZAÇÃO (DTO -> Entity Existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "idUnidade", target = "unidade")
    @Mapping(source = "idMedicamento", target = "medicamento")
    void updateEntityFromDto(EstoqueRequestDTO dto, @MappingTarget Estoque entity);

}