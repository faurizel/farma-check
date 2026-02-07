package com.fiap.farmacheck.model.dto.disponibilidade;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MedicamentoIndisponivelEvent implements Serializable {

    private String nomeMedicamento;
    private String emailUsuario;
    private String nomeUsuario;
    private LocalDateTime dataPesquisa;

    public MedicamentoIndisponivelEvent() {
    }

    public MedicamentoIndisponivelEvent(String nomeMedicamento, String emailUsuario, String nomeUsuario, LocalDateTime dataPesquisa) {
        this.nomeMedicamento = nomeMedicamento;
        this.emailUsuario = emailUsuario;
        this.nomeUsuario = nomeUsuario;
        this.dataPesquisa = dataPesquisa;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public LocalDateTime getDataPesquisa() {
        return dataPesquisa;
    }

    public void setDataPesquisa(LocalDateTime dataPesquisa) {
        this.dataPesquisa = dataPesquisa;
    }

    @Override
    public String toString() {
        return "MedicamentoIndisponivelEvent{" +
                "nomeMedicamento='" + nomeMedicamento + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", dataPesquisa=" + dataPesquisa +
                '}';
    }
}
