package com.fiap.farmacheck.model.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginRequestDTO(
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatório")
        String senha
) {
}
