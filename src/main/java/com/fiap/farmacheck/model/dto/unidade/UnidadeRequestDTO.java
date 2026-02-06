package com.fiap.farmacheck.model.dto.unidade;

import com.fiap.farmacheck.model.enums.TipoUnidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UnidadeRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O cep é obrigatório")
        String cep,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @NotBlank(message = "O tipo de unidade é obrigatório")
        TipoUnidade tipoUnidade // UPA ou UBS
) {
}
