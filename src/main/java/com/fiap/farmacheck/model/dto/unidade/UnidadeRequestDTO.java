package com.fiap.farmacheck.model.dto.unidade;

import com.fiap.farmacheck.model.enums.TipoUnidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnidadeRequestDTO(

        @NotNull(message = "O nome é obrigatório")
        String nome,

        @NotNull(message = "O cep é obrigatório")
        String cep,

        @NotNull(message = "O telefone é obrigatório")
        String telefone,

        @NotNull(message = "O email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @NotNull(message = "O tipo de unidade é obrigatório")
        TipoUnidade tipoUnidade // UPA ou UBS
) {
}
