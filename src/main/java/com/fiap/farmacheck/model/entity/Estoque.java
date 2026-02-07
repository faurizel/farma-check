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
@Table(name = "estoque")
public class Estoque extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estoque_sequence")
    @SequenceGenerator(name = "estoque_sequence", sequenceName = "estoque_sequence", allocationSize = 1)
    int id;

    Integer quantidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidade_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_estoque_unidade"))
    Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_estoque_medicamento"))
    Medicamento medicamento;
}
