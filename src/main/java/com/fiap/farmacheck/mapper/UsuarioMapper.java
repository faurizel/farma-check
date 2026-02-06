package com.fiap.farmacheck.mapper;

import com.fiap.farmacheck.model.dto.usuario.UsuarioRequestDTO;
import com.fiap.farmacheck.model.dto.usuario.UsuarioResponseDTO;
import com.fiap.farmacheck.model.entity.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UnidadeMapperHelper.class, AuditoriaMapper.class})
public interface UsuarioMapper {

    // 1. CONVERSÃO (DTO -> Entity)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "idUnidade", target = "unidade")
    Usuario toEntity(UsuarioRequestDTO dto);

    // 2. CONVERSÃO (Entity -> DTO)
    @Mapping(target = "auditoria", source = ".")
    UsuarioResponseDTO toResponseDTO(Usuario entity);

    // 3. ATUALIZAÇÃO (DTO -> Entity Existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "criacao", ignore = true)
    @Mapping(target = "ultimaModificacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "idUnidade", target = "unidade")
    void updateEntityFromDto(UsuarioRequestDTO dto, @MappingTarget Usuario usuario);
}