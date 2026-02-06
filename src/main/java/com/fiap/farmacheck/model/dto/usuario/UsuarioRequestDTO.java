package com.fiap.farmacheck.model.dto.usuario;

import com.fiap.farmacheck.model.enums.TipoUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UsuarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatório")
        @Size(min = 5, max = 15, message = "senha deve ter entre 5 e 15 caracteres")
        String senha,

        @NotNull(message = "O tipo de usuario é obrigatório")
        TipoUsuario tipoUsuario, // PACIENTE ou ADMINISTRADOR

        @NotNull(message = "O ID da unidade é obrigatório")
        Integer idUnidade
) {
}
