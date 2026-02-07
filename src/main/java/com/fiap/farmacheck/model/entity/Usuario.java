package com.fiap.farmacheck.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.farmacheck.model.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_sequence")
    @SequenceGenerator(name = "usuario_sequence", sequenceName = "usuario_sequence", allocationSize = 1)
    int id;

    String nome;

    @Column(unique = true)
    String email;

    String senha;

    @Column(name = "tipo_usuario")
    @Enumerated(EnumType.STRING)
    TipoUsuario tipoUsuario;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_unidade"))
    Unidade unidade;

    public Usuario(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }
}
