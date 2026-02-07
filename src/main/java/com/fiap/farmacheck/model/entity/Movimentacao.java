package com.fiap.farmacheck.model.entity;

import com.fiap.farmacheck.model.enums.TipoMovimentacao;
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
@Table(name = "movimentacao")
public class Movimentacao extends Auditoria{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimentacao_sequence")
    @SequenceGenerator(name = "movimentacao_sequence", sequenceName = "movimentacao_sequence", allocationSize = 1)
    int id;

    Integer quantidade;

    @Column(name = "tipo_movimentacao")
    @Enumerated(EnumType.STRING)
    TipoMovimentacao tipoMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id",referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_movimentacao_usuario"))
    Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id",referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_movimentacao_estoque"))
    Estoque estoque;
}
