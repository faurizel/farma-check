package com.fiap.farmacheck.model.entity;

import com.fiap.farmacheck.model.enums.TipoUnidade;
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
public class Unidade extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unidade_sequence")
    @SequenceGenerator(name = "unidade_sequence", sequenceName = "unidade_sequence", allocationSize = 1)
    int id;

    String nome;

    String cep;

    String telefone;

    String email;

    @Column(name = "tipo_unidade")
    @Enumerated(EnumType.STRING)
    TipoUnidade tipoUnidade;
}
