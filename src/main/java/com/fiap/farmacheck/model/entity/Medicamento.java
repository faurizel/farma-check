package com.fiap.farmacheck.model.entity;

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
public class Medicamento extends Auditoria{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medicamento_sequence")
    @SequenceGenerator(name = "medicamento_sequence", sequenceName = "medicamento_sequence", allocationSize = 1)
    int id;

    String nome;

    @Column(name = "principio_ativo")
    String pricipioAtivo;

    String dosagem;

    String laboratorio;
}
